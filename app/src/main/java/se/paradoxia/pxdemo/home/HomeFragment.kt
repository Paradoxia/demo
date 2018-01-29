package se.paradoxia.pxdemo.home

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_home.*
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.service.ExternalSiteOpener
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-24.
 */
class HomeFragment : Fragment(), ExternalSiteOpener {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    companion object {
        fun newInstance(): Fragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        AndroidInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return activity.layoutInflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.init(this)
        val layoutManager = LinearLayoutManager(activity)
        recViewHome.layoutManager = layoutManager
        recViewHome.adapter = HomeRecyclerViewAdapter(homeViewModel.getCards())
    }

    override fun open(url: String) {
        val externalSiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        externalSiteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        this.startActivity(externalSiteIntent)
    }

}