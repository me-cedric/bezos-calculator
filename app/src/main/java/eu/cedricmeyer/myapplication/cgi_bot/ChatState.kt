package eu.cedricmeyer.myapplication.cgi_bot

interface IChatState: IPublicChatState {
    val currentChatbot: Chatbot?
    val varData: Any
    val lastBotMessageId: String?
    val lastBotMessageExists: Boolean
    val defaultLang: String?
    val chatId: String?
    fun setChatbot(chatbot: Chatbot)
    val values: MutableMap<String, Any?>
    fun getValue(name: String): Any?
    fun setValue(name: String, value: Any)
    fun addMessage(message: ChatMessage)
    fun completeChat()
}

interface IPublicChatState {
    val isWritting: Boolean
    val chatMessages: MutableList<ChatMessage>
    val name: String?
    val description: String?
    val picture: String?
    val variableNames: List<String?>?
}

class PublicChatState(
    override val isWritting: Boolean,
    override val chatMessages: MutableList<ChatMessage>,
    override val name: String?,
    override val description: String?,
    override val picture: String?,
    override val variableNames: List<String?>?
) : IPublicChatState

class ChatState(
    isWritting: Boolean = false,
    chatMessages: MutableList<ChatMessage> = mutableListOf(),
    currentChatbot: Chatbot? = null,
    varData: MutableMap<String, Any> = mutableMapOf(),
    previousBotMessageId: String? = null,
    var onChange: ((chatState: ChatState) -> Unit)? = null
) : IChatState {
    override var isWritting: Boolean = isWritting
        set(value) {
            field = value
            this.onChange?.invoke(this)
        }

    override var chatMessages: MutableList<ChatMessage> = chatMessages
        private set

    override var currentChatbot: Chatbot? = currentChatbot
        set(value) {
            field = value
            this.onChange?.invoke(this)
        }

    override var varData: MutableMap<String, Any> = varData
        private set(value) {
            field = value
            this.onChange?.invoke(this)
        }

    override var lastBotMessageId: String? = previousBotMessageId

    override val name: String?
        get() = this.currentChatbot?.name

    override val chatId: String?
        get() = this.currentChatbot?.objectId

    override val defaultLang: String
        get() = this.currentChatbot?.defaultLanguage ?: "en-US"

    override val picture: String?
        get() = this.currentChatbot?.picture

    override val description: String?
        get() = this.currentChatbot?.description

    override val lastBotMessageExists: Boolean
        get() = this.currentChatbot !== null && this.lastBotMessageId !== null

    override val values: MutableMap<String, Any?>
        get() {
            val vars: MutableMap<String, Any?> = mutableMapOf()
            val variableIds: MutableSet<String> = this.varData.keys
            // Get the name of the var
            this.variableNames?.forEach { variable: String? ->
                vars[variable!!] = if (variableIds.contains(variable) && this.varData.containsKey(variable)) this.varData[variable] else null
            }
            return vars
        }

    override val variableNames: List<String?>?
        get() {
            return this.currentChatbot?.messages
                ?.filter { msg: Message -> msg.collect !== null }
                ?.map { msg: Message -> msg.collect}
                ?.distinct()
        }

    fun findChatbotMessageById(id: String?): Message {
        val msg: Message? = this.currentChatbot?.messages
            ?.filter { message: Message -> message.id === id }
            ?.get(0)
        if (msg === null) {
            throw Error("Message with id ['${id}'] not found")
        }
        return Message(msg)
    }

    fun findMessageIndexById(id: String): Int {
        val messages: List<Message>? = this.currentChatbot?.messages
        if (messages === null) {
            throw Error("No messages found in the conversation")
        }
        messages.forEachIndexed { index: Int, message: Message ->
            if (message.id == id) {
                return index
            }
        }
        return -1
    }

    fun findMessageByIndex(messageIndex: Int): Message {
        val message: Message? = this.currentChatbot?.messages?.get(messageIndex)
        if (message === null) {
            throw Error("No message found at index [${messageIndex}] in the conversation")
        }
        return Message(message)
    }

    override fun setChatbot(chatbot: Chatbot) {
        this.varData = mutableMapOf() // reset
        this.currentChatbot = chatbot
        this.onChange?.invoke(this)
    }

    override fun getValue(name: String): Any? {
        return this.varData[name]
    }

    override fun setValue(name: String, value: Any) {
        this.varData[name] = value
        this.onChange?.invoke(this)
    }

    override fun addMessage(message: ChatMessage) {
        this.chatMessages.add(message)
        this.onChange?.invoke(this)
    }

    override fun completeChat() {
        this.currentChatbot = null
        this.lastBotMessageId = null
        this.onChange?.invoke(this)
    }

}
