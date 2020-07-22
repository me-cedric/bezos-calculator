package eu.cedricmeyer.myapplication.cgi_bot

interface IAttachment {
    val title: String
    val value: Any? /* String? | MutableList<String>? */
    val multiple: Boolean?
    val data: Any?
}

data class Attachment(
    override val title: String,
    override val value: Any? = null,
    override val multiple: Boolean? = null,
    override val data: Any? = null
) : IAttachment