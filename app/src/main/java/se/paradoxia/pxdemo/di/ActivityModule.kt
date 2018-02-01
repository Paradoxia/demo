package se.paradoxia.pxdemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.home.HomeViewLogicImpl
import se.paradoxia.pxdemo.service.HomeViewLogic
import se.paradoxia.pxdemo.service.PermissionService

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

        @Provides
        @JvmStatic
        fun provideHomeViewLogic(@ActivityContext context: Context, permissionService: PermissionService): HomeViewLogic {
            return HomeViewLogicImpl(context, permissionService)
        }

    }

}

