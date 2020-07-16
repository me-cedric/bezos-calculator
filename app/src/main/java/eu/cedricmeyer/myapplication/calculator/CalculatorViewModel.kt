package eu.cedricmeyer.myapplication.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel(private var displayState: MutableLiveData<Any> = MutableLiveData(),
                          private var id: MutableLiveData<String> = MutableLiveData(),
                          private var isPrivateMode: MutableLiveData<Boolean> = MutableLiveData()
) : ViewModel() {
}