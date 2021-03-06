package eu.cedricmeyer.myapplication.cgi_bot

interface ICgiBotConfiguration {
    val chatbots : MutableList<Chatbot>?
    val language: String?
    val storage: Any?
    val analyticsConfig: IAnalyticsConfig?
    val disableConsole: Boolean?
    val nextFunctions: Map<String, (nextCodes: MutableList<String>, variables: Any) -> String>? /*string | Promise<string>*/
    // ...
}

class CgiBotConfiguration(
    override var chatbots: MutableList<Chatbot> = mutableListOf(),
    override var language: String = "en-US",
    override var storage: Any? = null,
    override var analyticsConfig: IAnalyticsConfig? = null,
    override var disableConsole: Boolean? = false,
    override var nextFunctions: Map<String, (nextCodes: List<String>, variables: Any) -> String>? = emptyMap()
) : ICgiBotConfiguration {

    fun executeNextFunction(nextCodeFunction: String, codes: List<String>, values: Any): String? {
        return this.nextFunctions?.get(nextCodeFunction)?.invoke(codes, values)
    }
}
