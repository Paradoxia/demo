package se.paradoxia.pxdemo.home.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import org.mockito.Mockito
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.di.FragmentModule
import se.paradoxia.pxdemo.home.view.HomeViewLogic
import se.paradoxia.pxdemo.home.view.HomeViewLogicImpl
import se.paradoxia.pxdemo.service.PermissionService


/**
 * Created by mikael on 2018-01-31.
 */
@Module
abstract class HomeTestActivityModule {

    @ActivityContext
    @ContributesAndroidInjector(modules = [StubMainActivityModule::class, FragmentModule::class])
    abstract fun bindStubMainActivity(): MainActivity

    @Module
    object StubMainActivityModule {
        @Provides
        @ActivityContext
        @JvmStatic
        fun provideActivityContext(activity: MainActivity): Context {
            return activity
        }

        @Provides
        @JvmStatic
        fun provideHomeViewLogic(@ActivityContext context: Context, permissionService: PermissionService): HomeViewLogic {
            val spiedHomeViewLogic = Mockito.spy(HomeViewLogicImpl(context, permissionService))
            return spiedHomeViewLogic!!
        }

    }

}