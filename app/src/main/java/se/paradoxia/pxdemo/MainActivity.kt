package se.paradoxia.pxdemo

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import se.paradoxia.pxdemo.home.HomeFragment
import se.paradoxia.pxdemo.permission.PermissionResultReceiver
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class MainActivity : BaseActivity(), PermissionResultReceiver {

    var activeFragment: Fragment? = null

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

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()
        addFragmentToActivity(fragmentManager, getDefaultFragment(), R.id.flPage)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getDefaultFragment() : Fragment {
        return HomeFragment.newInstance()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction = manager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
        this.activeFragment = fragment
    }

    // (This Activity) onRequestPermissionsResult -> (Active Fragment) onRequestPermissionsResult -> (SomeReceiverLogic) onRequestPermissionsResult
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this.activeFragment is PermissionResultReceiver) {
            (this.activeFragment as PermissionResultReceiver).onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
