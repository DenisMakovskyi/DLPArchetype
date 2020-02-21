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
class StatelessViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var provider = providers[modelClass]
        if (provider == null) {
            for (entry in providers.entries) {
                if (modelClass::class.java.isAssignableFrom(entry.key)) {
                    provider = entry.value
                    break
                }
            }
        }

        requireNotNull(provider) { "Unknown model class ${modelClass.name}" }

        try {
            return provider.get() as T

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}