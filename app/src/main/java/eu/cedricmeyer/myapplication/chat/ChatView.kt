package eu.cedricmeyer.myapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.InputStream

class ChatView : Fragment() {
    private lateinit var chatViewViewModel: ChatViewModel
    private lateinit var adapter: ChatMessageAdapter

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

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
        }
        chatState.observe(viewLifecycleOwner, Observer {
            val messages: List<ChatMessage> = (it?.chatMessages?.toList() ?: listOf())
            println(messages)
            recycler_view.apply {
                // set the custom adapter to the RecyclerView
                adapter = ChatMessageAdapter(messages)
            }
        })

        sendTextMessage("onboarding", true)
    }

    private fun verifySms(nextCodes: List<String>, variables: Any): String {
        return nextCodes[1]
    }

    fun sendTextMessage(text: String, hidden: Any? = false) {
        this.chatViewViewModel.sendTextMessage(text, hidden)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatView()
    }
}