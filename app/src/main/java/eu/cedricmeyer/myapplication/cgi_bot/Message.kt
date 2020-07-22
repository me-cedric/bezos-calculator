package eu.cedricmeyer.myapplication.cgi_bot

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

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
    override val attachment: Attachment?
    override val beforeSend: String?
    override val afterSend: String?
    override val customField: String?
    val collect: String?
    val nextCodeFunction: String?
    val nextOptions: MutableList<NextOption>?
    val next: String? /* String? | "complete" */
    override val fromUser: Boolean?
    override val collectType: String? /* 'string' | 'number' | 'tel' | 'email' | 'password' | 'file' | 'date' | 'native' | 'quickReply' */
    override val collectPattern: Any?
    override val delay: Number?
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Message(
    override val id: String,
    override val text: String,
    override val translations: Map<String, String>?,
    override val epoch: Number?,
    override val attachment: Attachment?,
    override val beforeSend: String?,
    override val afterSend: String?,
    override val customField: String?,
    override val collect: String?,
    override val nextCodeFunction: String?,
    override val nextOptions: MutableList<NextOption>?,
    override val next: String? = "complete",
    override val fromUser: Boolean?,
    override val collectType: String? = "string",
    override val collectPattern: Any?,
    override val delay: Number? = 0
): IMessage {
    constructor(msg: IMessage): this(
        msg.id,
        msg.text,
        msg.translations,
        msg.epoch,
        msg.attachment,
        msg.beforeSend,
        msg.afterSend,
        msg.customField,
        msg.collect,
        msg.nextCodeFunction,
        msg.nextOptions,
        msg.next,
        msg.fromUser,
        msg.collectType,
        msg.collectPattern,
        msg.delay
    )

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