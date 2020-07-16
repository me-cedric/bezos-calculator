package eu.cedricmeyer.myapplication.cgi_bot

interface ChatMessage {
    var id: String
    var text: String
    var translations: Map<String, String>?
    var epoch: Number?
    var attachment: IAttachment?
    var beforeSend: String?
    var afterSend: String?
    var customField: String?
    var fromUser: Boolean?
    var collectType: String? /* 'string' | 'number' | 'tel' | 'email' | 'password' | 'file' | 'date' | 'native' | 'quickReply' */
    var collectPattern: Any?
    var delay: Number?
}

interface IMessage : ChatMessage {
    override var id: String
    override var text: String
    override var translations: Map<String, String>?
    override var epoch: Number?
    override var attachment: IAttachment?
    override var beforeSend: String?
    override var afterSend: String?
    override var customField: String?
    var collect: String?
    var nextCodeFunction: String?
    var nextOptions: Array<INextOption>?
    var next: String? /* String? | "complete" */
    override var fromUser: Boolean?
    override var collectType: String? /* 'string' | 'number' | 'tel' | 'email' | 'password' | 'file' | 'date' | 'native' | 'quickReply' */
    override var collectPattern: Any?
    override var delay: Number?
}
