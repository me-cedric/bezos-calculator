package eu.cedricmeyer.myapplication

import android.app.Dialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

enum class CurrencyValue(val value: Double) {
    Euro(0.88),
    Dollar(1.0)
}

class MainActivity : AppCompatActivity(), OnAlertOneButtonClickListener {

    private val bezosWorth: Double = 160_000_000_000.0 // dollars

    private val customDialogOneButton by lazy {
        CustomAlertDialogOneButton(this, this)
    }

    override fun okClickListener(dialogId: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ArrayAdapter.createFromResource(
            this,
            R.array.currency_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        floatingActionButton.isEnabled = editText1.text.toString().trim().isNotEmpty()
        floatingActionButton.setOnClickListener {
            val spinnerVal = spinner.selectedItem.toString()
            var modifier: Double = 1.0
            when(spinnerVal) {
                "Euro" -> modifier = CurrencyValue.Euro.value
                "Dollar" -> modifier = CurrencyValue.Dollar.value
            }
            val yourPrice: Double = editText1.text.toString().toDouble() / (bezosWorth * modifier)
            customDialogOneButton.show(
                title = "This has a cost of",
                text = "${BigDecimal(yourPrice).toPlainString()} Jeff Bezos",
                dialogId = 1,
                buttonName = "Bah... it's not that much then."
            )
        }

        editText1.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                floatingActionButton.isEnabled = s.toString().trim().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Auto-generated method stub
            }
        })
    }
}