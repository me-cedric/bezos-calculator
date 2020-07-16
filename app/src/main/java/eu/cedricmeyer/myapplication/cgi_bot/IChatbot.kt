package eu.cedricmeyer.myapplication.cgi_bot

interface IChatbot {
    var objectId: String?
    var picture: String? // chatbot picture
    var name: String
    var description: String? // description of the script
    var defaultLanguage: String? // default language code of the chatbot
    var otherLanguages: Collection<String>? // default language code of the chatbot
    var updatedAt: String? // UTC date
    var isFallback: Boolean? // is the default script to use if nothing else is matched
    var triggers: Collection<ITrigger>? // what text/message should trigger a conversation
    var messages: Collection<IMessage>? // Threads/branch of the script that can reference each one
}

interface ITrigger {
    var pattern: String // the text or pattern match in a message that should trigger a dialog
    var type: TriggerType // should be matched as a string or a regex
    var objectId: Int
}

enum class TriggerType(var type: String) {
    STRING("string"),
    REGEX("regex");

    companion object {
        fun from(findValue: String): TriggerType = TriggerType.values().first { it.type == findValue }
    }
}