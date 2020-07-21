package eu.cedricmeyer.myapplication.cgi_bot

interface IChatbot {
    val objectId: String?
    val picture: String? // chatbot picture
    val name: String
    val description: String? // description of the script
    val defaultLanguage: String? // default language code of the chatbot
    val otherLanguages: MutableList<String>? // default language code of the chatbot
    val updatedAt: String? // UTC date
    val isFallback: Boolean? // is the default script to use if nothing else is matched
    val triggers: MutableList<ITrigger>? // what text/message should trigger a conversation
    val messages: MutableList<IMessage>? // Threads/branch of the script that can reference each one
}

data class Chatbot(
    override var objectId: String? = null,
    override var picture: String? = null,
    override var name: String,
    override var description: String? = null,
    override var defaultLanguage: String = "en-US",
    override var otherLanguages: MutableList<String> = mutableListOf(),
    override var updatedAt: String? = null,
    override var isFallback: Boolean = false,
    override var triggers: MutableList<ITrigger> = mutableListOf(),
    override var messages: MutableList<IMessage> = mutableListOf()
) : IChatbot

interface IChatbotTriggers {
    val trigger: ITrigger
    val chatbotId: String
}

data class ChatbotTriggers(override val trigger: ITrigger, override val chatbotId: String) : IChatbotTriggers

interface ITrigger {
    val pattern: String // the text or pattern match in a message that should trigger a dialog
    val type: TriggerType // should be matched as a string or a regex
    val objectId: Int
}

enum class TriggerType(var type: String) {
    STRING("string"),
    REGEX("regex");

    companion object {
        fun from(findValue: String): TriggerType = TriggerType.values().first { it.type == findValue }
    }
}