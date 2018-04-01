package se.paradoxia.pxdemo.home.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Provider

class TestViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) throw IllegalArgumentException("unknown model class $modelClass")
        try {
            return Mockito.spy(creator.get()) as T // Let's spy on ViewModel
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}