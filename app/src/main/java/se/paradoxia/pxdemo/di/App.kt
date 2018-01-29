package se.paradoxia.pxdemo.di

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import se.paradoxia.pxdemo.util.AllOpen
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-20.
 */
@AllOpen
class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
        initRealm()
        initAppComponent()
    }

    private fun initAppComponent() {

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun initRealm() {
        Realm.init(this)
    }

}