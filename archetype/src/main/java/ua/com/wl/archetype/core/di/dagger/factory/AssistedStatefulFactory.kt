package ua.com.wl.archetype.core.di.dagger.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

/**
 * @author Denis Makovskyi
 */

interface AssistedStatefulFactory<T: ViewModel> {

    fun create(savedStateHandle: SavedStateHandle): T
}