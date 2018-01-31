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

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        /*DaggerHomeTestAppComponent.builder()
                .application(this)
                .build()
                .inject(this)*/
    }

    fun setModules(testViewModelModule: TempViewModelModule) {

        DaggerHomeTestAppComponent.builder()
                .application(this)
                .viewModelModule(testViewModelModule)
                .build()
                .inject(this)
    }

}
