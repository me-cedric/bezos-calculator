package eu.cedricmeyer.myapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.CgiBot
import eu.cedricmeyer.myapplication.cgi_bot.CgiBotConfiguration
import eu.cedricmeyer.myapplication.cgi_bot.Chatbot
import io.ktor.http.ContentDisposition.Companion.File
import java.io.File
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
        var chat: Chatbot = mapper.readValue<Chatbot>(test, object : TypeReference<Chatbot>(){})
        val config: CgiBotConfiguration = CgiBotConfiguration(chatbots = mutableListOf(chat))
//        val chatbot: CgiBot = CgiBot()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatView()
    }
}