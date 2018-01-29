package se.paradoxia.pxdemo.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import com.gojuno.koptional.Optional
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.AllOpen
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-24.
 */
@AllOpen
class HomeViewModel @Inject constructor(private val contentService: ContentService,
                                        private val sharedPreferencesService: SharedPreferencesService) : ViewModel() {

    val someText = ObservableField<String>()
    val profileImage = ObservableField<Int>()

    val profileName = ObservableField<String>()

    val cardProfilerHeader = CardProfilerHeader(this)

    val cardAboutMe = CardAboutMe()

    fun init() {


        profileImage.set(R.drawable.profile_image)
        profileName.set("Mikael Olsson")

        cardProfilerHeader.init()

        loadContent()

    }

    @Suppress("UNUSED_PARAMETER")
    fun selectLangEN(view: View?) {
        sharedPreferencesService.putString("language", "en")
        loadContent()
    }

    @Suppress("UNUSED_PARAMETER")
    fun selectLangSV(view: View?) {
        sharedPreferencesService.putString("language", "sv")
        loadContent()
    }

    internal fun loadContent() {
        val language = sharedPreferencesService.getString("language", groupKey = null, defaultValue = "en")
        contentService.fetchAboutMe().subscribe({ t: Optional<AboutMeResponse> ->
            val aboutMeResponse: AboutMeResponse? = t.toNullable()
            if (aboutMeResponse != null) {
                cardAboutMe.aboutMeText.set(if (language == "en") aboutMeResponse.aboutMeEn?.text else aboutMeResponse.aboutMeSv?.text)
                cardAboutMe.aboutMeTitle.set(if (language == "en") aboutMeResponse.aboutMeEn?.title else aboutMeResponse.aboutMeSv?.title)
                cardAboutMe.aboutMeHeadline.set(if (language == "en") aboutMeResponse.aboutMeEn?.headline else aboutMeResponse.aboutMeSv?.headline)
            }
        })
    }

    fun setText(text: String) {
        someText.set(text)
    }

    fun getCards() = listOf(cardProfilerHeader, cardAboutMe, cardProfilerHeader, cardProfilerHeader, cardProfilerHeader, cardProfilerHeader)

    class CardProfilerHeader(val homeViewModel: HomeViewModel) {

        fun init() {
            profileImageRes.set(R.drawable.profile_image)
            profileName.set("Mikael Olsson")
        }

        val profileImageRes = ObservableField<Int>()
        val profileName = ObservableField<String>()

    }

    class CardAboutMe {
        val aboutMeTitle = ObservableField<String>()
        val aboutMeHeadline = ObservableField<String>()
        val aboutMeText = ObservableField<String>()
    }


}

