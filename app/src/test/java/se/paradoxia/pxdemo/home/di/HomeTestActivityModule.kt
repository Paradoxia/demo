package se.paradoxia.pxdemo.home.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.di.FragmentModule
import se.paradoxia.pxdemo.home.StubMainActivity


/**
 * Created by mikael on 2018-01-31.
 */
@Module
abstract class HomeTestActivityModule {

    @ActivityContext
    @ContributesAndroidInjector(modules = [StubMainActivityModule::class, FragmentModule::class])
    abstract fun bindStubMainActivity(): StubMainActivity

    @Module
    object StubMainActivityModule {
        @Provides
        @ActivityContext
        @JvmStatic
        fun provideActivityContext(activity: StubMainActivity): Context {
            return activity
        }

    }


}