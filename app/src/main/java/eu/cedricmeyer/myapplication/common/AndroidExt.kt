package eu.cedricmeyer.myapplication.common

import android.app.Activity
import android.content.Intent

import android.text.Editable
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import eu.cedricmeyer.myapplication.calculator.CalculatorActivity
import eu.cedricmeyer.myapplication.calculator.CalculatorView
import java.text.SimpleDateFormat
import java.util.*

internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


internal fun Activity.attachFragment(manager: FragmentManager, containerId: Int, view: Fragment, tag: String) {
    manager.beginTransaction()
            .replace(containerId, view, tag)
            .commitNowAllowingStateLoss()
}

internal fun Fragment.getCalendarTime(): String {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z")
    format.timeZone = cal.timeZone
    return format.format(cal.time)
}

internal fun Fragment.makeToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
}

internal fun Fragment.restartCurrentFeature() {
    val i = Intent(this.activity, CalculatorActivity::class.java)
//    when (this) {
//        is CalculatorView -> {
//            i = Intent(this.activity, CalculatorActivity::class.java)
//        }
//
//        //To Be Added
//
//        else -> {
//            i = Intent(this.activity, NoteListActivity::class.java)
//        }
//    }

    this.activity?.finish()
    startActivity(i)
}