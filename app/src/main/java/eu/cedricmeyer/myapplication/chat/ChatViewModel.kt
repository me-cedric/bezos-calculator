package eu.cedricmeyer.myapplication.chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.cedricmeyer.myapplication.cgi_bot.*


class ChatViewModelFactory(private val chatBots: MutableList<Chatbot>, private val nextFcts: Map<String, (nextCodes: List<String>, variables: Any) -> String>? = null) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(chatBots, nextFcts) as T
    }

}

class ChatViewModel(chatBots: MutableList<Chatbot>, nextFcts: Map<String, (nextCodes: List<String>, variables: Any) -> String>? = null) : ViewModel() {
    private var cgiBot: CgiBot
    private var chatState: PublicChatState? = null
    private var chatStateLiveData = MutableLiveData<PublicChatState?>()

    init {
        val config = CgiBotConfiguration(chatbots = chatBots, nextFunctions = nextFcts, language = "fr-FR")
        cgiBot = CgiBot(config)
        cgiBot.onChatChange { chatResponses: PublicChatState ->
            chatState = chatResponses
        }
        val message = ClientMessage("onboarding")
        cgiBot.sendMessage(message)
    }

    fun getInitialChatState(): MutableLiveData<PublicChatState?>{
        chatStateLiveData.value = chatState
        return chatStateLiveData
    }

    fun sendTextMessage(text: String) {
        val message = ClientMessage(text)
        cgiBot.sendMessage(message)
    }
}