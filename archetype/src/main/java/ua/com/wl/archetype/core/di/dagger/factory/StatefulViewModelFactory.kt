package ua.com.wl.archetype.core.di.dagger.factory

import android.os.Bundle

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.savedstate.SavedStateRegistryOwner

import dagger.Reusable

/**
 * @author Denis Makovskyi
 */

@Reusable
class StatefulViewModelFactory @Inject constructor(
    private val assistedFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedStatefulFactory<out ViewModel>>) {

    fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val factory = assistedFactories[modelClass]
                requireNotNull(factory) { "Unknown model class ${modelClass.name}" }

                try {
                    return factory.create(handle) as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}