package se.paradoxia.pxdemo

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import se.paradoxia.pxdemo.home.HomeFragment
import se.paradoxia.pxdemo.util.CustomDebugTree
import se.paradoxia.pxdemo.util.ReleaseTree
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasFragmentInjector {

    // Activity is responsible for Fragment injections
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun fragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Must be done BEFORE super.onCreate so fragmentInjector is available to restored sub views (fragments etc.)
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        addFragmentToActivity(fragmentManager, HomeFragment.newInstance(), R.id.flPage)

    }

    private fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction = manager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }

}
