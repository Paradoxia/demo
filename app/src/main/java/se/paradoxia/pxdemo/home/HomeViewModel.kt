package se.paradoxia.pxdemo.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.service.ContentService
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-24.
 */
class HomeViewModel @Inject constructor(val contentService: ContentService) : ViewModel() {

    val someText = ObservableField<String>()
    val profileImage = ObservableField<Int>()

    val profileName = ObservableField<String>()

    val cardProfilerHeader = CardProfilerHeader()

    val cardAboutMe = CardAboutMe()

    fun init() {
        profileImage.set(R.drawable.profile_image)
        profileName.set("Mikael Olsson")

        cardProfilerHeader.init()
        cardAboutMe.init()

        contentService.fetchAboutMe().subscribe({t: AboutMeResponse? ->

            println("x")

        })

    }

    fun setText(text: String) {
        someText.set(text)
    }

    fun getCards() = listOf(cardProfilerHeader, cardAboutMe, cardProfilerHeader, cardProfilerHeader, cardProfilerHeader, cardProfilerHeader)

    class CardProfilerHeader {

        fun init() {
            profileImageRes.set(R.drawable.profile_image)
            profileName.set("Mikael Olsson")
        }

        val profileImageRes = ObservableField<Int>()
        val profileName = ObservableField<String>()

    }

    class CardAboutMe {

        fun init() {

        }


    }


}

