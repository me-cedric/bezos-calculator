package eu.cedricmeyer.myapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.*
import java.io.InputStream

class ChatView : Fragment() {
    private lateinit var chatViewViewModel: ChatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val chatStream: InputStream? = context?.applicationContext?.resources?.openRawResource(+ R.raw.testchat)
        val mapper = jacksonObjectMapper()
        val chat: Chatbot = mapper.readValue<Chatbot>(chatStream, object : TypeReference<Chatbot>(){})
        val nextFcts: Map<String, (nextCodes: List<String>, variables: Any) -> String>? =
            mapOf("verifySms" to ::verifySms)
        val myViewModel: ChatViewModel by viewModels { ChatViewModelFactory(chatBots = mutableListOf(chat), nextFcts = nextFcts) }
        chatViewViewModel = myViewModel
        val chatState: LiveData<PublicChatState?> = chatViewViewModel.getInitialChatState()
        chatState.observe(viewLifecycleOwner, Observer {
//            txt1.setText("Count is "+it)
        })

    }

    private fun verifySms(nextCodes: List<String>, variables: Any): String {
        return nextCodes[1]
    }

    fun sendTextMessage(text : String) {
        this.chatViewViewModel.sendTextMessage(text)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatView()
    }
}