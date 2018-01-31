package se.paradoxia.pxdemo.home.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import se.paradoxia.pxdemo.home.HomeFragmentTest
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, HomeTestActivityModule::class, HomeTestAppModule::class])
interface HomeTestAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: HomeTestApp): Builder

        // DON'T use @BindsInstance otherwise Dagger will create the instance
        fun setHomeTestAppModule(homeTestAppModule: HomeTestAppModule): Builder

        fun build(): HomeTestAppComponent
    }

    fun inject(app: HomeTestApp)

    fun inject(any: HomeFragmentTest)

}