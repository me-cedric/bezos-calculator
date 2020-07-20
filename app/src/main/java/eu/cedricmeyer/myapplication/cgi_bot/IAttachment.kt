package eu.cedricmeyer.myapplication.cgi_bot

interface IAttachment {
    val title: String
    val value: Any? /* String? | MutableList<String>? */
    val multiple: Boolean?
    val data: Any?
}