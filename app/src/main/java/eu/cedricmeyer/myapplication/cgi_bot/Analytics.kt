package eu.cedricmeyer.myapplication.cgi_bot

interface IAnalyticsConfig {
    val app: String?
    val version: String?
    val debug: Boolean?
    val plugins: MutableList<IPlugin>?
}

class AnalyticsConfig(
    override var app: String? = null,
    override var version: String? = null,
    override var debug: Boolean? = null,
    override var plugins: MutableList<IPlugin>? = null
) : IAnalyticsConfig

interface IPlugin {
    val name: String
    val EVENTS: Any?
    val config: Any?
    val enabled: Boolean?
    fun initialize(vararg params: MutableList<Any>): Any
    fun page(vararg params: MutableList<Any>): Any
    fun track(vararg params: MutableList<Any>): Any
    fun identify(vararg params: MutableList<Any>): Any
    fun loaded(vararg params: MutableList<Any>): Any
    fun ready(vararg params: MutableList<Any>): Any
}

interface IPageData {
    val title: String?
    val url: String?
    val path: String?
    val search: String?
    val width: String?
    val height: String?
    val hash: String?
    val referrer: String?
}

interface IAnalytics {
    fun identify(userId: String, traits: Any, options: Any, callback: (params: Any) -> Any) // Identify a user. This will trigger `identify` calls in any installed plugins and will set user data in localStorage
    fun track(eventName: String, payload: Any, options: Any, callback: (params: Any) -> Any) // Track an analytics event. This will trigger `track` calls in any installed plugins
    fun page(data: IPageData, options: Any, callback: (params: Any) -> Any) // Trigger page view. This will trigger `page` calls in any installed plugins
    fun user(key: String): Any // Get user data
    fun getState(callback: (params: Any) -> Any): () -> Unit
    fun enablePlugin(plugins: Any /* String | MutableList<String> */, callback: (params: Any) -> Any) // Enable analytics plugin
    fun disablePlugin(plugins: Any /* String | MutableList<String> */, callback: (params: Any) -> Any) // Disable analytics plugin
}

class Analytics(config: IAnalyticsConfig? = AnalyticsConfig()) : IAnalytics {
    override fun identify(
        userId: String,
        traits: Any,
        options: Any,
        callback: (params: Any) -> Any
    ) {
        TODO("Not yet implemented")
    }

    override fun track(
        eventName: String,
        payload: Any,
        options: Any,
        callback: (params: Any) -> Any
    ) {
        TODO("Not yet implemented")
    }

    override fun page(data: IPageData, options: Any, callback: (params: Any) -> Any) {
        TODO("Not yet implemented")
    }

    override fun user(key: String): Any {
        TODO("Not yet implemented")
    }

    override fun getState(callback: (params: Any) -> Any): () -> Unit {
        TODO("Not yet implemented")
    }

    override fun enablePlugin(plugins: Any, callback: (params: Any) -> Any) {
        TODO("Not yet implemented")
    }

    override fun disablePlugin(plugins: Any, callback: (params: Any) -> Any) {
        TODO("Not yet implemented")
    }

}
