package se.paradoxia.pxdemo.home

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import se.paradoxia.pxdemo.di.ActivityModule
import se.paradoxia.pxdemo.di.ViewModelModuleInterface
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, HomeTestAppModule::class, ActivityModule::class, ViewModelModuleInterface::class])
interface HomeTestAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: HomeTestApp): Builder

        @BindsInstance
        fun setViewModelModule(viewModelModule: ViewModelModuleInterface) : Builder

        fun build(): HomeTestAppComponent
    }

    fun inject(app: HomeTestApp)

}