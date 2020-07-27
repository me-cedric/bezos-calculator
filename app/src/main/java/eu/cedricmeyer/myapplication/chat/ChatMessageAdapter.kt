package eu.cedricmeyer.myapplication.chat

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.ChatMessage
import kotlinx.android.synthetic.main.fragment_message.view.*


class ChatMessageAdapter(private val chatMessages: List<ChatMessage>)
    : RecyclerView.Adapter<ChatMessageAdapter.ChatMessageHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatMessageHolder {
        val inflatedView = parent.inflate(R.layout.fragment_message, false)
        return ChatMessageHolder(inflatedView)
    }

    override fun getItemCount(): Int = chatMessages.size

    override fun onBindViewHolder(holder: ChatMessageHolder, position: Int) {
        val itemMessage = chatMessages[position]
        holder.bindMessage(itemMessage)
    }

    class ChatMessageHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var chatMessage: ChatMessage? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            // OPEN SOMETHING
//            val context = itemView.context
//            val showPhotoIntent = Intent(context, PhotoActivity::class.java)
//            showPhotoIntent.putExtra(PHOTO_KEY, photo)
//            context.startActivity(showPhotoIntent)
        }

        fun bindMessage(message: ChatMessage) {
            this.chatMessage = message
            view.itemText.text = message.text
        }

        companion object {
            private val MSG_KEY = "MESSAGE"
        }
    }
}