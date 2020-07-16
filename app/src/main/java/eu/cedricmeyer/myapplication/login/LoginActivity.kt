package eu.cedricmeyer.myapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eu.cedricmeyer.myapplication.calculator.CalculatorActivity
import eu.cedricmeyer.myapplication.R


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(view: View) {
        val intent = Intent(this, CalculatorActivity::class.java)
        intent.putExtra("bloop", "bleep")
        startActivity(intent)
    }
}