package se.paradoxia.pxdemo.home

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import com.gojuno.koptional.Optional
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.ViewTypeMapper
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-24.
 */
@AllOpen
class HomeViewModel @Inject constructor(private val contentService: ContentService,
                                        private val sharedPreferencesService: SharedPreferencesService) : ViewModel() {

    val cardProfileHeader = CardProfileHeader(this)
    val cardAboutMe = CardAboutMe()

    var homeViewAction: HomeViewAction? = null

    var language: String? = null

    fun init(homeViewAction: HomeViewAction, profileImageResourceUri: String) {
        this.homeViewAction = homeViewAction
        cardProfileHeader.init(profileImageResourceUri)
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

    fun openExternalSite(view: View?) {
        if (view?.tag != null) {
            homeViewAction?.openExternalSite(view.tag as String)
        }
    }

    fun saveToStorage(view: View?) {
        if (view?.tag != null) {
            homeViewAction?.saveToStorage(view.tag as String, if (language == "sv") "se" else language!!)
        }
    }

    internal fun loadContent() {
        language = sharedPreferencesService.getString("language", groupKey = null, defaultValue = "en")!!

        contentService.fetchAboutMe().subscribe({ response: Optional<AboutMeResponse> ->
            val aboutMeResponse: AboutMeResponse? = response.toNullable()
            if (aboutMeResponse != null) {
                cardAboutMe.update(language!!, aboutMeResponse)
            }
        })

        contentService.fetchInfoCard().subscribe({ response: Optional<InfoCardResponse> ->
            val infoCardResponse: InfoCardResponse? = response.toNullable()
            if (infoCardResponse != null) {
                cardProfileHeader.update(language!!, infoCardResponse)
            }
        })

    }

    fun getCards() = listOf(cardProfileHeader, cardAboutMe)

    fun getViewTypeMap() = listOf(
            ViewTypeMapper(CardAboutMe::class.java,
                    R.layout.card_about_me,
                    CardAboutMeViewHolder::class.java),
            ViewTypeMapper(CardProfileHeader::class.java,
                    R.layout.card_profile_header,
                    CardProfileHeaderViewHolder::class.java)
    )

    class CardProfileHeader(val homeViewModel: HomeViewModel) {
        val downloadFile = ObservableField<String>()
        val downloadText = ObservableField<String>()
        val facebook = ObservableField<String>()
        val instagram = ObservableField<String>()
        val google = ObservableField<String>()
        val twitter = ObservableField<String>()
        val linkedin = ObservableField<String>()
        val name = ObservableField<String>()
        val profileImage = ObservableField<String>()
        val role = ObservableField<String>()

        /**
         * Setting profile image using url from resources
         */
        fun init(profileImageResourceUri: String) {
            profileImage.set(profileImageResourceUri)
        }

        fun update(language: String, infoCardResponse: InfoCardResponse) {
            downloadFile.set(if (language == "en") BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile?.en else BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile?.sv)
            downloadText.set(if (language == "en") infoCardResponse.downloadText?.en else infoCardResponse.downloadText?.sv)
            facebook.set(BuildConfig.FACEBOOK_EXTERNAL_URL + infoCardResponse.facebook)
            instagram.set(infoCardResponse.instagram)
            google.set(infoCardResponse.google)
            twitter.set(infoCardResponse.twitter)
            linkedin.set(infoCardResponse.linkedin)
            name.set(infoCardResponse.name)
            profileImage.set(BuildConfig.IMAGE_BASE_URL + infoCardResponse.profileImage?.x2)
            role.set(if (language == "en") infoCardResponse.role?.en else infoCardResponse.role?.sv)
        }

    }

    class CardAboutMe {
        val aboutMeTitle = ObservableField<String>()
        val aboutMeHeadline = ObservableField<String>()
        val aboutMeText = ObservableField<String>()

        fun update(language: String, aboutMeResponse: AboutMeResponse) {
            aboutMeText.set(if (language == "en") aboutMeResponse.aboutMeEn?.text else aboutMeResponse.aboutMeSv?.text)
            aboutMeTitle.set(if (language == "en") aboutMeResponse.aboutMeEn?.title else aboutMeResponse.aboutMeSv?.title)
            aboutMeHeadline.set(if (language == "en") aboutMeResponse.aboutMeEn?.headline else aboutMeResponse.aboutMeSv?.headline)
        }
    }


}

