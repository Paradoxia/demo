package se.paradoxia.pxdemo.di

import android.app.Activity
import android.app.Application
import android.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasFragmentInjector
import se.paradoxia.pxdemo.util.AllOpen
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-30.
 */

@AllOpen
class TestApp : Application(), HasActivityInjector, HasFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun fragmentInjector(): AndroidInjector<Fragment>  = fragmentInjector

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        DaggerTestAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

}
