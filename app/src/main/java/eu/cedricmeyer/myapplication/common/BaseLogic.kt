package eu.cedricmeyer.myapplication.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

object DispatcherProvider {
    fun provideUIContext(): CoroutineContext {
        return Dispatchers.Main
    }

    fun provideIOContext(): CoroutineContext {
        return Dispatchers.IO
    }
}

/**
 * Why use a base class? To both share implementation (properties and functions), and enforce a contract (interface) for all listener classes
 */
abstract class BaseLogic(val dispatcher: DispatcherProvider) {

    protected lateinit var jobTracker: Job
}