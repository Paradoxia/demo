package se.paradoxia.pxdemo.di

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.home.view.HomeViewLogic
import se.paradoxia.pxdemo.home.view.HomeViewLogicImpl
import se.paradoxia.pxdemo.service.PermissionService

@Module
internal abstract class ActivityModule {

    @ActivityContext
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentModule::class])
    abstract fun bindMainActivity(): MainActivity

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Module
    object MainActivityModule {
        @Provides
        @ActivityContext
        @JvmStatic
        fun provideActivityContext(activity: MainActivity): Context {
            return activity
        }

        @Provides
        @JvmStatic
        fun provideHomeViewLogic(@ActivityContext context: Context, permissionService: PermissionService): HomeViewLogic {
            return HomeViewLogicImpl(context, permissionService)
        }

    }

}

