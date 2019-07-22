package ua.com.wl.archetype.core.android.service

import android.os.IBinder
import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection

/**
 * @author Denis Makovskyi
 */

open class ServiceConnector<S : Service>(val onServiceConnectionListener: OnServiceConnectionListener<S>) : ServiceConnection {

    interface OnServiceConnectionListener<S : Service> {

        fun onServiceConnected(service: S, binder: IBinder)

        fun onServiceDisconnected(componentName: ComponentName?)
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        binder?.let { onServiceConnectionListener.onServiceConnected((it as ServiceBinder).getService(), it) }
    }

    override fun onServiceDisconnected(componentName: ComponentName?) =
        onServiceConnectionListener.onServiceDisconnected(componentName)
}