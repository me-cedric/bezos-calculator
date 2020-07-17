package eu.cedricmeyer.myapplication.cgi_bot

interface IChatState {
    val isWritting: Boolean
    val chatMessages: MutableList<IChatMessage>
    val currentChatbot: IChatbot?
    val varData: Any
    val lastBotMessageId: String?
    val lastBotMessageExists: Boolean
    val name: String?
    val description: String?
    val picture: String?
    val defaultLang: String?
    val chatId: String?
    fun setChatbot(chatbot: IChatbot)
}

interface IPublicChatState {
    val isWritting: Boolean
    val chatMessages: MutableList<IChatMessage>
    val name: String?
    val description: String?
    val picture: String?
    val variableNames: MutableList<String>?
}

class PublicChatState(
    override val isWritting: Boolean,
    override val chatMessages: MutableList<IChatMessage>,
    override val name: String?,
    override val description: String?,
    override val picture: String?,
    override val variableNames: MutableList<String>?
) : IPublicChatState

class ChatState(
    override var isWritting: Boolean,
    override var chatMessages: MutableList<IChatMessage>,
    override var currentChatbot: IChatbot?,
    override var varData: Any,
    var previousBotMessageId: String?
) : IChatState {

    override val name: String?
        get() = this.currentChatbot?.name

    override val chatId: String?
        get() = this.currentChatbot?.objectId

    override val defaultLang: String?
        get() = this.currentChatbot?.defaultLanguage

    override val picture: String?
        get() = this.currentChatbot?.picture

    override val description: String?
        get() = this.currentChatbot?.description

    override val lastBotMessageExists: Boolean
        get() = this.currentChatbot !== null && this.lastBotMessageId !== null

    override val lastBotMessageId: String? = null

    init {
//        this.lastBotMessageId = previousBotMessageId
    }

    override fun setChatbot(chatbot: IChatbot) {
        TODO("Not yet implemented")
    }

}
