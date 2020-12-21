package ua.com.wl.archetype.core.android.bus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
object Bus {
    
    val publisher = PublishSubject.create<BusEvent>()

    fun send(event: BusEvent) = publisher.onNext(event)
    
    fun toObservable(): Observable<BusEvent> = publisher
}