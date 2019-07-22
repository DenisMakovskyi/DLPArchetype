package ua.com.wl.archetype.core.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * @author Denis Makovskyi
 */

class MainThreadExecutor : Executor {

    companion object {

        private val INSTANCE: MainThreadExecutor = MainThreadExecutor()

        @JvmStatic
        fun getInstance(): MainThreadExecutor = INSTANCE
    }

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        command?.let { if (Looper.getMainLooper() == Looper.myLooper()) it.run() else handler.post(it) }
    }
}