package eu.cedricmeyer.myapplication.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.*
import java.io.InputStream

class ChatViewModel(application: Application, val chatState: LiveData<ChatState>) : AndroidViewModel(application) {
    init {
        val test: InputStream? = getApplication<Application>().applicationContext.resources.openRawResource(+ R.raw.testchat)
        val mapper = jacksonObjectMapper()
        val chat: Chatbot = mapper.readValue<Chatbot>(test, object : TypeReference<Chatbot>(){})
        val nextFcts: Map<String, (nextCodes: List<String>, variables: Any) -> String>? =
            mapOf("verifySms" to ::verifySms)
        val config = CgiBotConfiguration(chatbots = mutableListOf(chat), nextFunctions = nextFcts, language = "fr-FR")
        val chatbot = CgiBot(config)
        chatbot.onChatChange { chatResponses: PublicChatState ->
            println("!!-------------------------------------------------------------------------!!")
            chatResponses.chatMessages.forEach { msg -> println(msg.text) }
        }
        val message = ClientMessage("onboarding")
        chatbot.sendMessage(message)
    }

    private fun verifySms(nextCodes: List<String>, variables: Any): String {
        return nextCodes[1]
    }
}