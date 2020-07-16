package eu.cedricmeyer.myapplication.calculator

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import eu.cedricmeyer.myapplication.CustomAlertDialogOneButton
import eu.cedricmeyer.myapplication.OnAlertOneButtonClickListener
import eu.cedricmeyer.myapplication.R
import kotlinx.android.synthetic.main.fragment_calculator.*
import java.math.BigDecimal

enum class CurrencyValue(val value: Double) {
    Euro(0.88),
    Dollar(1.0)
}

class CalculatorView : Fragment(),
    OnAlertOneButtonClickListener {

    private val bezosWorth: Double = 160_000_000_000.0 // dollars

    private val customDialogOneButton by lazy {
        CustomAlertDialogOneButton(activity, this)
    }

    override fun okClickListener(dialogId: Int) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(
            view.context,
            R.array.currency_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        floatingActionButton.isEnabled = editText1.text.toString().trim().isNotEmpty()

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

    fun onCompute() {
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

    companion object {
        @JvmStatic
        fun newInstance() =
            CalculatorView()
    }
}