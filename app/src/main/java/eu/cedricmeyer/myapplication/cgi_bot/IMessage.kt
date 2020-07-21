package eu.cedricmeyer.myapplication.cgi_bot

interface IChatMessage {
    val id: String
    val text: String
    val translations: Map<String, String>?
    val epoch: Number?
    val attachment: IAttachment?
    val beforeSend: String?
    val afterSend: String?
    val customField: String?
    val fromUser: Boolean?
    val collectType: String? /* 'string' | 'number' | 'tel' | 'email' | 'password' | 'file' | 'date' | 'native' | 'quickReply' */
    val collectPattern: Any?
    val delay: Number?
}

data class ChatMessage(
    override val id: String,
    override val text: String,
    override val translations: Map<String, String>? = null,
    override val epoch: Number? = null,
    override val attachment: IAttachment? = null,
    override val beforeSend: String? = null,
    override val afterSend: String? = null,
    override val customField: String? = null,
    override val fromUser: Boolean? = false,
    override val collectType: String? = null,
    override val collectPattern: Any? = null,
    override val delay: Number? = 0
) : IChatMessage

interface IMessage : IChatMessage {
    override val id: String
    override val text: String
    override val translations: Map<String, String>?
    override val epoch: Number?
    override val attachment: IAttachment?
    override val beforeSend: String?
    override val afterSend: String?
    override val customField: String?
    val collect: String?
    val nextCodeFunction: String?
    val nextOptions: MutableList<INextOption>?
    val next: String? /* String? | "complete" */
    override val fromUser: Boolean?
    override val collectType: String? /* 'string' | 'number' | 'tel' | 'email' | 'password' | 'file' | 'date' | 'native' | 'quickReply' */
    override val collectPattern: Any?
    override val delay: Number?
}

class Message(
    msg: IMessage
): IMessage {
    override val id: String = msg.id
    override val text: String = msg.text
    override val translations = msg.translations
    override val epoch = msg.epoch
    override val attachment = msg.attachment
    override val beforeSend = msg.beforeSend
    override val afterSend = msg.afterSend
    override val customField = msg.customField
    override val collect = msg.collect
    override val nextCodeFunction = msg.nextCodeFunction
    override val nextOptions = msg.nextOptions
    override val next = msg.next ?: "complete"
    override val fromUser = msg.fromUser
    override val collectType = msg.collectType ?: "string"
    override val collectPattern = msg.collectPattern
    override val delay = msg.delay ?: 0

    fun getLocaleText(locale: String, defaultLocale: String = "en-US"): String {
        if (defaultLocale === locale) {
            return this.text
        }
        return this.translations?.get(locale) ?: this.text
    }
}

interface IClientMessage {
    var message: String
    var hidden: Any? /* Boolean? | String */
    var file: String?
}
data class ClientMessage(
    override var message: String,
    override var hidden: Any? = false,
    override var file: String? = null
) : IClientMessage