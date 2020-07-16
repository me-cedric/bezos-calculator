package eu.cedricmeyer.myapplication.common

import android.app.Activity
import android.content.Intent
import eu.cedricmeyer.myapplication.calculator.CalculatorActivity
import eu.cedricmeyer.myapplication.login.LoginActivity

internal fun startCalculatorFeature(activity: Activity?, noteId: String, isPrivate: Boolean) {
    val i = Intent(activity, CalculatorActivity::class.java)
    activity?.startActivity(i)
}

internal fun startLoginFeature(activity: Activity?) {
    val i = Intent(activity, LoginActivity::class.java)
    activity?.startActivity(i)
            .also { activity?.finish() }
}

