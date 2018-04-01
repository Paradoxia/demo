package se.paradoxia.pxdemo

import android.app.Fragment
import android.app.FragmentManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.BottomNavigationView
import se.paradoxia.pxdemo.home.view.HomeView
import se.paradoxia.pxdemo.permission.PermissionResultReceiver
import se.paradoxia.pxdemo.personalinfo.view.PersonalInfoView
import se.paradoxia.pxdemo.util.AllOpen


@AllOpen
class MainActivity : BaseActivity(), PermissionResultReceiver, AppActionReceiver {

    var activeFragment: Fragment? = null

    var binding: ViewDataBinding? = null

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        addFragmentToActivity(fragmentManager, getDefaultFragment(), R.id.flPage)
    }

    override fun registerAppActionReceiver(appAction: AppAction) {
        binding!!.setVariable(BR.appAction, appAction)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getDefaultFragment(): Fragment {
        return HomeView.newInstance()
        //return PersonalInfoView.newInstance()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction = manager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
        this.activeFragment = fragment
    }

    // (This Activity) onRequestPermissionsResult -> (Active Fragment) onRequestPermissionsResult -> (SomeReceiverLogic) onRequestPermissionsResult
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this.activeFragment is PermissionResultReceiver) {
            (this.activeFragment as PermissionResultReceiver).onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }
    }
}
