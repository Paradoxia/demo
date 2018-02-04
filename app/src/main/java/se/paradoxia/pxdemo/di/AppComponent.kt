package se.paradoxia.pxdemo.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}


