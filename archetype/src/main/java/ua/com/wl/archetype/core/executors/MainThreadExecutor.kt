package ua.com.wl.archetype.core.executors

import java.util.concurrent.Executor

import android.os.Handler
import android.os.Looper

/**
 * @author Denis Makovskyi
 */

class MainThreadExecutor : Executor {

    companion object {

        val INSTANCE: MainThreadExecutor = MainThreadExecutor()
    }

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        command?.let { if (Looper.getMainLooper() == Looper.myLooper()) it.run() else handler.post(it) }
    }
}