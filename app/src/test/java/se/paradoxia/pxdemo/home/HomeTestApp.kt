package se.paradoxia.pxdemo.home

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import se.paradoxia.pxdemo.util.AllOpen
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-30.
 */

@AllOpen
class HomeTestApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    fun setModules(homeTestAppModule : HomeTestAppModule,  viewModelModule: HomeTestViewModelModuleInterface) {
        DaggerHomeTestAppComponent.builder()
                .application(this)
                .setViewModelModule(viewModelModule)
                .setHomeTestAppModule(homeTestAppModule)
                .build()
                .inject(this)
    }

}
