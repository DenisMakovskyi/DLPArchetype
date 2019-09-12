@file:Suppress("UNCHECKED_CAST")

package ua.com.wl.archetype.core.di.dagger.factory

import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author Denis Makovskyi
 */

@Singleton
open class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]
        if (creator == null) {
            for (entry in creators.entries) {
                if (modelClass::class.java.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }

        requireNotNull(creator) { "Unknown model class $modelClass" }

        try {
            return creator.get() as T

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}