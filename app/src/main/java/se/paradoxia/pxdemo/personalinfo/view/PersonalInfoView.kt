package se.paradoxia.pxdemo.personalinfo.view

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_personalinfo.*
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.personalinfo.viewmodel.PersonalInfoViewModel
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.FlexibleRecyclerViewAdapter
import javax.inject.Inject

/**
 * Created by mikael on 2018-02-11.
 */
@AllOpen
class PersonalInfoView : Fragment() {

    @Inject
    lateinit var personalInfoViewModel: PersonalInfoViewModel

    companion object {
        fun newInstance(): Fragment {
            return PersonalInfoView()
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
        return activity.layoutInflater.inflate(R.layout.fragment_personalinfo, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalInfoViewModel.init()

        val layoutManager = LinearLayoutManager(activity)
        recViewPersonalInfo.layoutManager = layoutManager
        recViewPersonalInfo.adapter = FlexibleRecyclerViewAdapter(
            personalInfoViewModel.getViewTypeMap(),
            personalInfoViewModel.getCards()
        )
    }

}