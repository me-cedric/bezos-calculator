package eu.cedricmeyer.myapplication.cgi_bot

import java.util.*

interface ICgiBot {
    val analytics: Any
    val bootCompleteHandlers: List<() -> Any>
    val booted: Boolean
    val config: ICgiBotConfiguration
    val chatStateChangedCallback: ((chatResponses: IPublicChatState) -> Any)?
}

class CgiBot(
    private var config: CgiBotConfiguration,
    private var analytics: Any = Analytics(config.analyticsConfig),
    private var bootCompleteHandlers: MutableList<(cgiBot: CgiBot) -> Any> = mutableListOf()
) {
    private var booted: Boolean = false
    private var chatStateChangedCallback: ((chatResponses: IPublicChatState) -> Any)? = null
    private var chatState = ChatState()
        set(value) {
            if (this.chatState !== value) {
                this.raiseChangedEvent(value)
            }
            field = value
        }

    var messageBeingHandled: Boolean = false
        private set

    init {
//        this.configureStorage()
        this.signalBootComplete()
    }

    fun setLang(lang: String) {
        this.config.language = lang.toUpperCase(Locale.getDefault())
    }

    fun addMessage(chatMessage: IChatMessage) {
        this.chatState.chatMessages.add(chatMessage)
        if (!chatMessage.fromUser!!) {
            this.chatState.lastBotMessageId = chatMessage.id
        }
//        this.raiseChangedEvent(this.chatState)
    }

    fun onChatChange(callback: (chatResponses: IPublicChatState) -> Any) {
        this.chatStateChangedCallback = callback
    }

    private fun raiseChangedEvent(chatState: IChatState) {
        this.chatStateChangedCallback?.invoke(PublicChatState(chatState.isWritting, chatState.chatMessages, chatState.name, chatState.description, chatState.picture, chatState.variablesNames))
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

    fun addDialog(dialog: IChatbot) {
        // add the actual dialog
        this.config.chatbots.add(dialog)
    }

    // async
    fun sendMessage(clientMessage: ClientMessage) {
        val ( message, hidden, file ) = clientMessage
        // todo this.analytics.track('message-received')

        if (hidden != true) {
            // todo this.analytics.track('massage-hidden')
            val chatMessage: IChatMessage = ChatMessage(
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
                /*await */this.runPreviousCheck(file || message)
                this.messageBeingHandled = false
                return
            }
            await this.testTrigger(message)
            this.messageBeingHandled = false
        }
    }

    private fun evaluateTrigger(text: String): IChatbot {
        // todo this.analytics.track('trigger-evaluation')
        fun mapTriggers(chatbots: MutableList<IChatbot>?): List<ChatbotTriggers> {
            val triggers: MutableList<ChatbotTriggers> = mutableListOf()
            chatbots?.forEach{ chatbot: IChatbot? ->
                chatbot?.triggers?.forEach { trigger: ITrigger ->
                    triggers.add(
                        ChatbotTriggers(
                            trigger = trigger,
                            chatbotId = chatbot.objectId!!
                        )
                    )
                }
            }
            // Sort in the order of descending pattern length
            return triggers.sortBy { it.trigger.pattern.length }
        }
        val res: MutableList<String> = mutableListOf()
        val bots: MutableList<IChatbot> = this.config.chatbots
        val chatTriggers: MutableList<ChatbotTriggers> = mapTriggers(bots)

        // check regular expressions or keywords
        chatTriggers.forEach { triggerData ->
            val ( trigger ) = triggerData
            var found: Any = false
            try {
                val test = Regex(if (trigger.type === TriggerType.REGEX) trigger.pattern else "^${trigger.pattern}\\b", RegexOption.IGNORE_CASE)
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
        if (!res.length) {
            // find a script set with is_fallback true
            bots.forEach((chatbot: Chatbot) => {
                if (chatbot.isFallback) {
                    res.push(chatbot.objectId as string)
                }
            })
        }

        if (res.length) {
            // this is the script that will be triggered.
            return bots.filter((chatbot: Chatbot) => chatbot.objectId === res[0])[0]
        }
    }
}