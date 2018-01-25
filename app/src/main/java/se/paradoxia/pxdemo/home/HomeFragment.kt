package se.paradoxia.pxdemo.home

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import se.paradoxia.pxdemo.R

import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-24.
 */
class HomeFragment : Fragment() {

    @Inject lateinit var homeViewModel: HomeViewModel

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
        homeViewModel.init()
        val layoutManager = LinearLayoutManager(activity)
        recViewHome.layoutManager = layoutManager
        recViewHome.adapter = HomeRecyclerViewAdapter(homeViewModel.getCards())
    }

}