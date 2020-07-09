package eu.cedricmeyer.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import kotlinx.android.synthetic.main.custom_alert_one_button_dialog.*

interface OnAlertOneButtonClickListener {
    fun okClickListener(dialogId: Int = -1)
}

class CustomAlertDialogOneButton(activity: Activity?, private val alertOneButtonClickListener: OnAlertOneButtonClickListener): Dialog(activity!!) {
    private var title = ""
    private var text = ""
    private var dialogId = -1
    private var buttonName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_alert_one_button_dialog)
        setCancelable(false)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewsClickListenerInit()
    }

    override fun onStart() {
        initDialog()
        super.onStart()
    }

    private fun fillFields(title: String, text: String?, dialogId: Int, buttonName: String) {
        clearDialog()
        this.title = title
        this.text = text ?: ""
        this.dialogId = dialogId
        this.buttonName = buttonName
    }

    private fun clearDialog() {
        title = ""
        text = ""
    }

    private fun initDialog() {
        if (title.isNotBlank()) {
            tvAlertTitle.text = title
        }

        if (text.isNotBlank()) {
            tvAlertText.text = text
        }

        tvAlertButtonOk.text = buttonName
    }

    fun show(title: String, text: String?, dialogId: Int = -1, buttonName: String = Resources.getSystem().getString(R.string.ok)) {
        fillFields(title, text, dialogId, buttonName)
        super.show()
    }

    private fun viewsClickListenerInit() {
        tvAlertButtonOk.setOnClickListener {
            alertOneButtonClickListener.okClickListener(dialogId)
            dismiss()
        }
    }
}