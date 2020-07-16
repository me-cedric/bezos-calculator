package eu.cedricmeyer.myapplication.cgi_bot

interface IAnalyticsConfig {
    var app: String?
    var version: String?
    var debug: Boolean?
    var plugins: Array<IPlugin>?
}

interface IPlugin {
    var name: String
    var EVENTS: Any?
    var config: Any?
    var enabled: Boolean?
    fun initialize(vararg params: Array<Any>): Any
    fun page(vararg params: Array<Any>): Any
    fun track(vararg params: Array<Any>): Any
    fun identify(vararg params: Array<Any>): Any
    fun loaded(vararg params: Array<Any>): Any
    fun ready(vararg params: Array<Any>): Any
}

interface ICgiBotConfiguration {
    var chatbots : Array<IChatbot>
    var language: String?
    var storage: Any?
    var analyticsConfig: IAnalyticsConfig?
    var disableConsole: Boolean?
    var nextFunctions: Map<String, (nextCodes: Array<String>, variables: Any) -> String>? /*string | Promise<string>*/
    // ...
}

class CgiBotConfiguration(conf: ICgiBotConfiguration) : ICgiBotConfiguration {
    override var chatbots: Array<IChatbot> = conf.chatbots
    override var language: String? = conf.language ?: "en-US"
    override var storage: Any? = conf.storage
    override var analyticsConfig: IAnalyticsConfig? = conf.analyticsConfig
    override var disableConsole: Boolean? = conf.disableConsole
    override var nextFunctions: Map<String, (nextCodes: Array<String>, variables: Any) -> String>? = conf.nextFunctions

    fun executeNextFunction(nextCodeFunction: String, codes: Array<String>, values: Any): String? {
        return this.nextFunctions?.get(nextCodeFunction)?.invoke(codes, values)
    }
}
