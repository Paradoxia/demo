package se.paradoxia.pxdemo.home.view

import android.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_home.*
import se.paradoxia.pxdemo.AppActionReceiver
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.permission.PermissionResultReceiver
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.FlexibleRecyclerViewAdapter
import javax.inject.Inject


/**
 * Created by mikael on 2018-01-24.
 */
@AllOpen
class HomeViewFragment : Fragment(), PermissionResultReceiver {

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var homeViewLogic: HomeViewLogic

    companion object {
        fun newInstance(): Fragment {
            return HomeViewFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        AndroidInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity!!.layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProviders.of(activity as FragmentActivity, viewModelFactory)
            .get(HomeViewModel::class.java)

        val applicationId = BuildConfig.APPLICATION_ID
        val profileImageResourceUri =
            Uri.parse("android.resource://$applicationId/" + R.drawable.profile_image)

        homeViewModel.init(this.homeViewLogic as HomeViewAction, profileImageResourceUri.toString())

        (activity as AppActionReceiver).registerAppActionReceiver(homeViewModel)
        val layoutManager = LinearLayoutManager(activity)
        recViewHome.layoutManager = layoutManager
        recViewHome.adapter = FlexibleRecyclerViewAdapter(
            homeViewModel.getViewTypeMap(),
            homeViewModel.getCards()
        )
    }

    // (Activity) -> (This Fragment) onRequestPermissionsResult -> (HomeViewLogic) onRequestPermissionsResult
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        homeViewLogic.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}