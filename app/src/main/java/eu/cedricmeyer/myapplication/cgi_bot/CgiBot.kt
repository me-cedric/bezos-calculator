package eu.cedricmeyer.myapplication.cgi_bot

class CgiBot(
    config: ICgiBotConfiguration
) {
    private var analytics: Any
    private var bootCompleteHandlers: List<() -> Any>
    private var booted: Boolean
    private var conf: CgiBotConfiguration

    init {
        config.disableConsole = false
        config.nextFunctions= emptyMap()
        config.language= "en-US"
        this.conf = CgiBotConfiguration(config)
        this.booted = false
        this.bootCompleteHandlers = listOf()
        this.analytics = Analytics(this.config.analyticsConfig || {})
//        this.configureStorage()
//        this.createProxy(new ChatState())
//        this.signalBootComplete()
    }
}