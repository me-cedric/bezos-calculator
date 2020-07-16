package eu.cedricmeyer.myapplication.cgi_bot

interface Attachment {
    var title: String
    var value: Any? /* String? | Array<String>? */
    var multiple: Boolean?
    var data: Any?
}