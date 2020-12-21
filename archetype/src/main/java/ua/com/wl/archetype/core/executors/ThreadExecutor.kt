package ua.com.wl.archetype.core.executors

import java.util.concurrent.*

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
interface ThreadExecutor : Executor {

    class JobThreadFactory : ThreadFactory {

        private var counter: Int = 0

        override fun newThread(r: Runnable?): Thread = Thread(r, "android_${counter++}")
    }

    class DefaultWorker : ThreadExecutor {

        private val threadPoolExecutor: ThreadPoolExecutor = ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Int.MAX_VALUE,
            TimeUnit.SECONDS.toSeconds(10), TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>(), JobThreadFactory())

        override fun execute(command: Runnable?) {
            command?.let { threadPoolExecutor.execute(it) }
        }
    }
}