package eu.cedricmeyer.myapplication.cgi_bot

interface ICgiBot {
    val analytics: Any
    val bootCompleteHandlers: List<() -> Any>
    val booted: Boolean
    val config: ICgiBotConfiguration
    val chatStateChangedCallback: ((chatResponses: IPublicChatState) -> Any)?
}

class CgiBot(
    config: CgiBotConfiguration
) {
    private var analytics: Any
    private var bootCompleteHandlers: List<() -> Any>
    private var booted: Boolean
    private var config: CgiBotConfiguration
    private var chatStateChangedCallback: ((chatResponses: IPublicChatState) -> Any)? = null
    private var chatState = ChatState()
        set(value) {
            if (this.chatState !== value) {
                this.raiseChangedEvent(value)
            }
            field = value
        }

    init {
        config.disableConsole = false
        config.nextFunctions= emptyMap()
        config.language= "en-US"
        this.config = config
        this.booted = false
        this.bootCompleteHandlers = listOf()
        this.analytics = Analytics(config.analyticsConfig)
//        this.configureStorage()
//        this.signalBootComplete()
    }

    fun setLang(lang: String) {
        this.config.language = lang.toUpperCase()
    }

    fun addMessage(chatMessage: IChatMessage) {
        this.chatState.chatMessages.add(chatMessage)
        if (!chatMessage.fromUser) {
            this.chatState.previousBotMessageId = chatMessage.id
        }
        this.raiseChangedEvent(this.chatState)
    }

    private fun raiseChangedEvent(chatState: IChatState) {
        this.chatStateChangedCallback?.invoke(PublicChatState(chatState.isWritting, chatState.chatMessages, chatState.name, chatState.description, chatState.picture, chatState.variablesNames))
    }
}