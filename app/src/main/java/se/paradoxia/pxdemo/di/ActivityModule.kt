package se.paradoxia.pxdemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity

@Module
abstract class ActivityModule {

    @ActivityContext
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentModule::class])
    abstract fun bindMainActivity(): MainActivity

    @Module
    object MainActivityModule {
        @Provides
        @ActivityContext
        @JvmStatic
        fun provideActivityContext(activity: MainActivity): Context {
            return activity
        }

    }

}

