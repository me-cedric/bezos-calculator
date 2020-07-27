package eu.cedricmeyer.myapplication.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.common.attachFragment
import kotlinx.android.synthetic.main.fragment_chat.*

private const val VIEW = "CALCULATOR"

class ChatActivity : AppCompatActivity() {

    private lateinit var chatView: ChatView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        this.chatView = this.supportFragmentManager.findFragmentByTag(VIEW) as ChatView?
            ?: ChatView.newInstance()

        attachFragment(supportFragmentManager, R.id.root_activity_calculator, chatView, VIEW)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun sendMessage(view: View) {
        this.chatView.sendTextMessage(textField.text.toString())
        textField.text?.clear()
    }
}