package eu.cedricmeyer.myapplication.cgi_bot

interface ICgiBotConfiguration {
    val chatbots : MutableList<IChatbot>
    val language: String?
    val storage: Any?
    val analyticsConfig: IAnalyticsConfig?
    val disableConsole: Boolean?
    val nextFunctions: Map<String, (nextCodes: MutableList<String>, variables: Any) -> String>? /*string | Promise<string>*/
    // ...
}

class CgiBotConfiguration(
    override var chatbots: MutableList<IChatbot>,
    override var language: String? = "en-US",
    override var storage: Any?,
    override var analyticsConfig: IAnalyticsConfig?,
    override var disableConsole: Boolean = false,
    override var nextFunctions: Map<String, (nextCodes: List<String>, variables: Any) -> String> = emptyMap()
) : ICgiBotConfiguration {

    fun executeNextFunction(nextCodeFunction: String, codes: List<String>, values: Any): String? {
        return this.nextFunctions[nextCodeFunction]?.invoke(codes, values)
    }
}
