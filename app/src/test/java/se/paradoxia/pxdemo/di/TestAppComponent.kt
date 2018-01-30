package se.paradoxia.pxdemo.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, TestAppModule::class, TestActivityModule::class])
interface TestAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: TestApp): Builder

        fun build(): TestAppComponent
    }

    fun inject(app: TestApp)
}