package eu.cedricmeyer.myapplication.cgi_bot

import io.ktor.mustache.MustacheContent
import java.util.*

class CgiBot(
    private var config: CgiBotConfiguration,
    private var bootCompleteHandlers: MutableList<(cgiBot: CgiBot) -> Any> = mutableListOf()
) {
    private var analytics: Any = Analytics(config.analyticsConfig)
    private var booted: Boolean = false
    private var chatStateChangedCallback: ((chatResponses: PublicChatState) -> Any)? = null
    private var chatState = ChatState(onChange = {chatData -> this.raiseChangedEvent(chatData)})
        set(value) {
            if (this.chatState !== value) {
                this.raiseChangedEvent(value)
            }
            field = value
        }

    private val locale: String
        get() = this.config.language

    var messageBeingHandled: Boolean = false
        private set

    init {
//        this.configureStorage()
        this.signalBootComplete()
    }

    fun setLang(lang: String) {
        this.config.language = lang.toUpperCase(Locale.getDefault())
    }

    fun addMessage(chatMessage: ChatMessage) {
        this.chatState.addMessage(chatMessage)
        if (!chatMessage.fromUser!!) {
            this.chatState.lastBotMessageId = chatMessage.id
        }
//        this.raiseChangedEvent(this.chatState)
    }

    fun onChatChange(callback: (chatResponses: PublicChatState) -> Unit) {
        this.chatStateChangedCallback = callback
    }

    private fun raiseChangedEvent(chatState: IChatState) {
        this.chatStateChangedCallback?.invoke(PublicChatState(chatState.isWritting, chatState.chatMessages, chatState.name, chatState.description, chatState.picture, chatState.variableNames))
    }

    fun ready(handler: (cgiBot: CgiBot) -> Any) {
        if (this.booted) {
            handler.invoke(this)
        } else {
            this.bootCompleteHandlers.add(handler)
        }
    }

    private fun signalBootComplete() {
        this.booted = true
        this.bootCompleteHandlers.forEach{ handler -> handler.invoke(this) }
    }

    fun addDialog(dialog: Chatbot) {
        // add the actual dialog
        this.config.chatbots.add(dialog)
    }

    // async
    fun sendMessage(clientMessage: ClientMessage) {
        val ( message, hidden, file ) = clientMessage
        // todo this.analytics.track('message-received')

        if (hidden != true) {
            // todo this.analytics.track('massage-hidden')
            val chatMessage = ChatMessage(
                id = ShortId.generate(),
                text = if (hidden === "password") "••••••••••" else message,
                attachment = Attachment(
                    title = "file",
                    value = file
                ),
                fromUser = true,
                epoch = (System.currentTimeMillis() / 1000)
            )
            this.addMessage(chatMessage)
        }
        if (!this.messageBeingHandled) {
            // Handle the current step
            this.messageBeingHandled = true
            if (this.chatState.lastBotMessageExists) {
                /*await */this.runPreviousCheck(file ?: message)
                this.messageBeingHandled = false
                return
            }
            /*await*/this.testTrigger(message)
            this.messageBeingHandled = false
        }
    }

    private fun evaluateTrigger(text: String): Chatbot? {
        // todo this.analytics.track('trigger-evaluation')
        fun mapTriggers(chatbots: MutableList<Chatbot>?): MutableList<ChatbotTriggers> {
            val triggers: MutableList<ChatbotTriggers> = mutableListOf()
            chatbots?.forEach{ chatbot: Chatbot? ->
                chatbot?.triggers?.forEach { trigger: Trigger ->
                    triggers.add(
                        ChatbotTriggers(
                            trigger = trigger,
                            chatbotId = chatbot.objectId!!
                        )
                    )
                }
            }
            // Sort in the order of descending pattern length
            triggers.sortBy { it.trigger.pattern.length }
            return triggers
        }
        val res: MutableList<String> = mutableListOf()
        val bots: MutableList<Chatbot>? = this.config.chatbots
        val chatTriggers: MutableList<ChatbotTriggers> = mapTriggers(bots)

        // check regular expressions or keywords
        chatTriggers.forEach { triggerData ->
            val ( trigger ) = triggerData
            var found: Any = false
            try {
                val test = Regex(if (trigger.type == TriggerType.REGEX) trigger.pattern else "^${trigger.pattern}\\b", RegexOption.IGNORE_CASE)
                found = text.matches(test)
            } catch (err: Error) {
                // todo this.analytics.track('trigger-regex-error')
                println(err)
            }

            if (found != false) {
                res.add(triggerData.chatbotId)
            }
        }

        // check for no results...
        if (res.size == 0) {
            // find a script set with is_fallback true
            bots?.forEach {chatbot: IChatbot ->
                if (chatbot.isFallback !== null) {
                    res.add(chatbot.objectId!!)
                }
            }
        }

        if (res.size > 0) {
            // this is the script that will be triggered.
            return bots?.filter { chatbot: Chatbot -> chatbot.objectId === res[0] }?.get(0)
        }
        return null
    }

    private /*async*/ fun testTrigger(message: String) {
        val chatbot: Chatbot? = this.evaluateTrigger(message)
        if (chatbot !== null) {
            return this.beginDialog(chatbot)
        }
    }

    private /*async*/ fun beginDialog(chatbot: Chatbot) {
        // todo this.analytics.track('dialog-start', { chatbot })
        // todo this.analytics.track(`dialog-start-${chatbot.objectId}`)
        this.chatState.setChatbot(chatbot)
        // Run the first step
        return this.runStep(0)
    }

    private /*async*/ fun runStep(messageIndex: Int) {
        // Get the next mesage.
        val message: Message = this.chatState.findMessageByIndex(messageIndex)
        // todo this.analytics.track('running-step', { message })
        // todo /*await*/ this.toggleTyping(message.delay)
        val chatMessage = ChatMessage(
            id = message.id,
            translations = message.translations,
            attachment = message.attachment,
            beforeSend = message.beforeSend,
            afterSend = message.afterSend,
            customField = message.customField,
            collectType = message.collectType,
            collectPattern = message.collectPattern,
            delay = message.delay,
            text = MustacheContent(message.getLocaleText(this.locale, this.chatState.defaultLang), this.chatState.values).template,
            fromUser = false,
            epoch = (System.currentTimeMillis() / 1000)
        )
        // todo this.analytics.track(`message-sent`)
        // todo this.analytics.track(`conversation-${this.chatState.chatId}-message-sent-${message.id}`)
        this.addMessage(chatMessage)
        if (message.collect == null) {
            // todo this.analytics.track('no-collect', { chatMessage })
            return this.runPreviousCheck()
        }
    }

//    private fun timeout(ms: Number) {
//        return new Promise((resolve) => setTimeout(resolve, ms))
//    }
//
//    private async fun toggleTyping(duration: Int = 400) {
//        // todo this.analytics.track(`toggling-typing`)
//        this.chatState.isWritting = true
//        await this.timeout(duration)
//        this.chatState.isWritting = false
//    }
    private /*async*/ fun runPreviousCheck(result: String = "") {
        // Get the last message the bot
        val message: Message = this.chatState.findChatbotMessageById(this.chatState.lastBotMessageId)
        // todo this.analytics.track('previous-check', { message })
        // Capture the user input value into the array
        if (message.collect !== null) {
            this.chatState.setValue(message.collect, result)
        }
        if (message.next === "complete") {
            // todo this.analytics.track('next-complete')
            return this.chatState.completeChat()
        }
        // Handle conditions of previous step
        val path: String? = /*await*/ this.getNextPath(message)
        // Take chosen path
        if (path !== null) {
            return this.runStep(this.chatState.findMessageIndexById(path))
        }
        // todo this.analytics.track('default-complete')
        // End conversation if no path is achieved
        return this.chatState.completeChat()
    }

    private /*async*/ fun getNextPath(message: Message): String? {
        var path: String? = null
        // If there are different options
        if (message.nextOptions !== null) {
            // If the options should return a function validation
            path = /*await*/ this.checkNextFunction(message, message.nextOptions)
        }
        // Use the default path if one is set
        if (path == null) {
            path = message.next
        }
        return path
    }

    private /*async*/ fun checkNextFunction(message: Message, options: MutableList<NextOption>): String? {
        val finder = NextFinder(options, this.config)
        // If the options should return a function validation
        if (message.nextCodeFunction != null) {
            return finder.findNextFromCode(message.nextCodeFunction, this.chatState)
        }
        return finder.findNextFromRegex(this.chatState)
    }
}