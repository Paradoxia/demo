package se.paradoxia.pxdemo.home.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import org.mockito.Mockito
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.di.FragmentModule
import se.paradoxia.pxdemo.home.HomeViewLogicImpl
import se.paradoxia.pxdemo.home.StubMainActivity
import se.paradoxia.pxdemo.service.HomeViewLogic
import se.paradoxia.pxdemo.service.PermissionService


/**
 * Created by mikael on 2018-01-31.
 */
@Module
abstract class HomeTestActivityModule {

    @ActivityContext
    @ContributesAndroidInjector(modules = [StubMainActivityModule::class, FragmentModule::class])
    abstract fun bindStubMainActivity(): StubMainActivity

    companion object {
        var spiedHomeViewLogic : HomeViewLogic? = null
    }

    @Module
    object StubMainActivityModule {
        @Provides
        @ActivityContext
        @JvmStatic
        fun provideActivityContext(activity: StubMainActivity): Context {
            return activity
        }

        // Can't use @Singleton scope since it conflict with @ActivityContext scope. Solving it using static declaration
        @Provides
        @JvmStatic
        fun provideHomeViewLogic(@ActivityContext context: Context, permissionService: PermissionService): HomeViewLogic {
            if(spiedHomeViewLogic == null) {
                spiedHomeViewLogic = Mockito.spy(HomeViewLogicImpl(context, permissionService))
            }
            return spiedHomeViewLogic!!
        }

    }


}