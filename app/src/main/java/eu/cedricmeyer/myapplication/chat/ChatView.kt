package eu.cedricmeyer.myapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.*
import java.io.InputStream

class ChatView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val test: InputStream? = context?.resources?.openRawResource(+ R.raw.testchat)
        val mapper = jacksonObjectMapper()
        val chat: Chatbot = mapper.readValue<Chatbot>(test, object : TypeReference<Chatbot>(){})
        val nextFcts: Map<String, (nextCodes: List<String>, variables: Any) -> String>? =
            mapOf("verifySms" to ::verifySms)
        val config = CgiBotConfiguration(chatbots = mutableListOf(chat), nextFunctions = nextFcts)
        val chatbot = CgiBot(config)
        chatbot.onChatChange { chatResponses: PublicChatState ->
            println("!!-------------------------------------------------------------------------!!")
            println(chatResponses.toString())
        }
        val message = ClientMessage("onboarding")
        chatbot.sendMessage(message)
    }

    fun verifySms(nextCodes: List<String>, variables: Any): String {
        return nextCodes[1]
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatView()
    }
}