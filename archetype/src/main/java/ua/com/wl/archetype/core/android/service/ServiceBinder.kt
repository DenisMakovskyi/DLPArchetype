package ua.com.wl.archetype.core.android.service

import android.os.Binder
import android.app.Service

/**
 * @author Denis Makovskyi
 */

abstract class ServiceBinder : Binder() {

    abstract fun <S : Service> getService(): S
}