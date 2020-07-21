package eu.cedricmeyer.myapplication.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.common.attachFragment

private const val VIEW = "CALCULATOR"

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val view = this.supportFragmentManager.findFragmentByTag(VIEW) as ChatView?
            ?: ChatView.newInstance()

        attachFragment(supportFragmentManager, R.id.root_activity_calculator, view, VIEW)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}