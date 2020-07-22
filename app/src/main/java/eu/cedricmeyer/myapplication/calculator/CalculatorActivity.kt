package eu.cedricmeyer.myapplication.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import eu.cedricmeyer.myapplication.CustomAlertDialogOneButton
import eu.cedricmeyer.myapplication.OnAlertOneButtonClickListener
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.common.attachFragment
import java.math.BigDecimal

private const val VIEW = "CALCULATOR"

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val view = this.supportFragmentManager.findFragmentByTag(VIEW) as CalculatorView?
            ?: CalculatorView.newInstance()

        attachFragment(supportFragmentManager, R.id.root_activity_calculator, view, VIEW)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}