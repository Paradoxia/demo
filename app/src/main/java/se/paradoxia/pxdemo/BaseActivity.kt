package se.paradoxia.pxdemo

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import io.fabric.sdk.android.Fabric
import se.paradoxia.pxdemo.util.CustomDebugTree
import se.paradoxia.pxdemo.util.ReleaseTree
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mikael on 2018-02-01.
 */
abstract class BaseActivity : AppCompatActivity(), HasFragmentInjector {

    // Activity is responsible for Fragment injections
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun fragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {

        // Must be done BEFORE super.onCreate so fragmentInjector is available to restored sub views (fragments etc.)
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

    }
}