package se.paradoxia.pxdemo.home

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_home.*
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.service.PermissionService
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.FlexibleRecyclerViewAdapter
import javax.inject.Inject


/**
 * Created by mikael on 2018-01-24.
 */
@AllOpen
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var permissionService: PermissionService

    var homeFragmentLogic: HomeFragmentLogic? = null

    companion object {
        fun newInstance(): Fragment {
            val homeFragment = HomeFragment()
            return homeFragment
        }

        fun newInstance(homeFragmentLogic: HomeFragmentLogic): Fragment {
            val homeFragment = HomeFragment()
            homeFragment.homeFragmentLogic = homeFragmentLogic
            return homeFragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        AndroidInjection.inject(this)
        if (homeFragmentLogic == null) {
            this.homeFragmentLogic = HomeFragmentLogic(permissionService)
            this.homeFragmentLogic!!.setActivity(this.activity as AppCompatActivity)
        } else {
            this.homeFragmentLogic!!.setActivity(this.activity as AppCompatActivity)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return activity.layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationId = BuildConfig.APPLICATION_ID
        val profileImageResourceUri = Uri.parse("android.resource://$applicationId/" + R.drawable.profile_image)
        homeViewModel.init(this.homeFragmentLogic as HomeViewAction, profileImageResourceUri.toString())
        val layoutManager = LinearLayoutManager(activity)
        recViewHome.layoutManager = layoutManager
        recViewHome.adapter = FlexibleRecyclerViewAdapter(homeViewModel.getViewTypeMap(), homeViewModel.getCards())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        homeFragmentLogic!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}