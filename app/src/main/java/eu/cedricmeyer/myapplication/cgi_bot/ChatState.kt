package eu.cedricmeyer.myapplication.cgi_bot

interface IChatState {
    val isWritting: Boolean
    val chatMessages: MutableList<IChatMessage>
    val currentChatbot: Chatbot?
    val varData: Any
    val lastBotMessageId: String?
    val lastBotMessageExists: Boolean
    val name: String?
    val description: String?
    val picture: String?
    val defaultLang: String?
    val chatId: String?
    fun setChatbot(chatbot: Chatbot)
    val variablesNames: List<String?>?
    val values: MutableMap<String, Any?>
}

interface IPublicChatState {
    val isWritting: Boolean
    val chatMessages: MutableList<IChatMessage>
    val name: String?
    val description: String?
    val picture: String?
    val variableNames: List<String?>?
}

class PublicChatState(
    override val isWritting: Boolean,
    override val chatMessages: MutableList<IChatMessage>,
    override val name: String?,
    override val description: String?,
    override val picture: String?,
    override val variableNames: List<String?>?
) : IPublicChatState

class ChatState(
    override var isWritting: Boolean = false,
    override var chatMessages: MutableList<IChatMessage> = mutableListOf(),
    override var currentChatbot: Chatbot? = null,
    override var varData: MutableMap<String, Any> = mutableMapOf(),
    previousBotMessageId: String? = null
) : IChatState {

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
            this.variablesNames?.forEach { variable: String? ->
                vars[variable!!] = if (variableIds.contains(variable) && this.varData.containsKey(variable)) this.varData[variable] else null
            }
            return vars
        }

    override val variablesNames: List<String?>?
        get() {
            return this.currentChatbot?.messages
                ?.filter { msg: IMessage -> msg.collect !== null }
                ?.map { msg: IMessage -> msg.collect}
                ?.distinct()
        }

    fun findChatbotMessageById(id: String?): Message {
        val msg: IMessage? = this.currentChatbot?.messages
            ?.filter { message: IMessage -> message.id === id }
            ?.get(0)
        if (msg === null) {
            throw Error("Message with id ['${id}'] not found")
        }
        return Message(msg)
    }

    fun findMessageIndexById(id: String): Int {
        val messages: List<IMessage>? = this.currentChatbot?.messages
        if (messages === null) {
            throw Error("No messages found in the conversation")
        }
        messages.forEachIndexed { index: Int, message: IMessage ->
            if (message.id === id) {
                return index
            }
        }
        return -1
    }

    fun findMessageByIndex(messageIndex: Int): Message {
        val message: IMessage? = this.currentChatbot?.messages?.get(messageIndex)
        if (message === null) {
            throw Error("No message found at index [${messageIndex}] in the conversation")
        }
        return Message(message)
    }

    override fun setChatbot(chatbot: Chatbot) {
        TODO("Not yet implemented")
    }

    fun getValue(name: String): Any? {
        return this.varData[name]
    }

    fun setValue(name: String, value: Any) {
        this.varData.set(name, value)
    }

    fun completeChat() {
        this.currentChatbot = null
        this.lastBotMessageId = null
    }

}
