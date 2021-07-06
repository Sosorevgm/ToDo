package com.sosorevgm.todo.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sosorevgm.todo.di.scopes.AppScope
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@AppScope
class ViewModelProviderFactory
@Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("Unchecked_Cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]

        creator?.let {
            for (entry in creators) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }

        return try {
            creator!!.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}