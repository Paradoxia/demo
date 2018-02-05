package se.paradoxia.pxdemo.home

import android.view.View
import com.gojuno.koptional.toOptional
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.amshove.kluent.*
import org.hamcrest.MatcherAssert
import org.hamcrest.beans.SamePropertyValuesAs
import org.junit.Test
import org.mockito.Mockito
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.Junit4TestBase
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.home.view.AboutMeViewHolder
import se.paradoxia.pxdemo.home.view.HomeViewAction
import se.paradoxia.pxdemo.home.view.ProfileHeaderViewHolder
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.rawResourceToInstance
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.ViewTypeMapper
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Created by mikael on 2018-01-29.
 */
class HomeViewModelTest : Junit4TestBase() {

    var contentService: ContentService? = null
    var testScheduler: TestScheduler? = null
    var homeViewModel: HomeViewModel? = null

    private val sharedPreferencesService: SharedPreferencesService =
        Mockito.mock(SharedPreferencesService::class.java)
    private val homeViewAction: HomeViewAction = Mockito.mock(HomeViewAction::class.java)
    private val view: View = Mockito.mock(View::class.java)

    private val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
    private val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

    override fun setUp() {

        contentService = Mockito.mock(ContentService::class.java)
        testScheduler = TestScheduler()
        homeViewModel = HomeViewModel(contentService!!, sharedPreferencesService)

        When calling contentService!!.fetchAboutMe() itReturns Observable.just(aboutMeResponse.toOptional()).observeOn(
            testScheduler
        )
        When calling contentService!!.fetchInfoCard() itReturns Observable.just(infoCardResponse.toOptional()).observeOn(
            testScheduler
        )
        When calling sharedPreferencesService.getString(any(), anyOrNull(), any()) itReturns "sv"

        // Will set and trigger all sort of things including observables that will not trigger until testScheduler calls "triggerActions"
        homeViewModel!!.init(homeViewAction, "not-important")

    }

    override fun tearDown() {
        contentService = null
        testScheduler = null
        homeViewModel = null
    }

    @Test
    fun shouldGetProfileAndAboutCards() {

        val expectedCards =
            listOf(HomeViewModel.CardProfileHeader(homeViewModel!!), HomeViewModel.CardAboutMe())
        val actualCards = homeViewModel!!.getCards()
        expectedCards.forEachIndexed { index, expectedCard ->
            assertEquals(expectedCard::class.java, actualCards[index]::class.java)
        }

    }

    @Test
    fun shouldGetViewTypeMapsForProfileAndAboutMe() {

        val expectedViewTypeMaps = listOf(
            ViewTypeMapper(
                HomeViewModel.CardAboutMe::class.java,
                R.layout.card_about_me,
                AboutMeViewHolder::class.java
            ),
            ViewTypeMapper(
                HomeViewModel.CardProfileHeader::class.java,
                R.layout.card_profile_header,
                ProfileHeaderViewHolder::class.java
            )
        )
        val actualViewTypMaps = homeViewModel!!.getViewTypeMap()

        expectedViewTypeMaps.forEachIndexed { index, expectedViewTypeMap ->
            MatcherAssert.assertThat(
                actualViewTypMaps[index],
                SamePropertyValuesAs.samePropertyValuesAs(expectedViewTypeMap)
            )
        }

    }

    @Test
    fun shouldFirstUseUriToInternalResourceImageAndAfterServerCallExternalUrl() {

        assertEquals("not-important", homeViewModel!!.cardProfileHeader.profileImage.get())

        testScheduler?.triggerActions()

        assertEquals(
            BuildConfig.IMAGE_BASE_URL + infoCardResponse.profileImage?.x2,
            homeViewModel!!.cardProfileHeader.profileImage.get()
        )

    }

    @Test
    fun shouldSetCardAboutMeObservableFieldsAfterServerResponse() {

        assertNull(homeViewModel!!.cardAboutMe.aboutMeText.get())
        assertNull(homeViewModel!!.cardAboutMe.aboutMeTitle.get())
        assertNull(homeViewModel!!.cardAboutMe.aboutMeHeadline.get())

        testScheduler?.triggerActions()

        assertEquals(aboutMeResponse.aboutMeSv?.text, homeViewModel!!.cardAboutMe.aboutMeText.get())
        assertEquals(
            aboutMeResponse.aboutMeSv?.title,
            homeViewModel!!.cardAboutMe.aboutMeTitle.get()
        )
        assertEquals(
            aboutMeResponse.aboutMeSv?.headline,
            homeViewModel!!.cardAboutMe.aboutMeHeadline.get()
        )

    }

    @Test
    fun shouldSetCardProfileHeaderObservableFieldsAfterServerResponse() {

        assertNull(homeViewModel!!.cardProfileHeader.downloadFile.get())
        assertNull(homeViewModel!!.cardProfileHeader.downloadText.get())
        assertNull(homeViewModel!!.cardProfileHeader.facebook.get())
        assertNull(homeViewModel!!.cardProfileHeader.instagram.get())
        assertNull(homeViewModel!!.cardProfileHeader.twitter.get())
        assertNull(homeViewModel!!.cardProfileHeader.linkedin.get())
        assertNull(homeViewModel!!.cardProfileHeader.name.get())
        assertNull(homeViewModel!!.cardProfileHeader.role.get())

        testScheduler?.triggerActions()

        assertEquals(
            BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile?.sv,
            homeViewModel!!.cardProfileHeader.downloadFile.get()
        )

        assertEquals(
            infoCardResponse.downloadText?.sv,
            homeViewModel!!.cardProfileHeader.downloadText.get()
        )

        assertEquals(
            BuildConfig.FACEBOOK_EXTERNAL_URL + infoCardResponse.facebook,
            homeViewModel!!.cardProfileHeader.facebook.get()
        )

        assertEquals(infoCardResponse.instagram, homeViewModel!!.cardProfileHeader.instagram.get())
        assertEquals(infoCardResponse.google, homeViewModel!!.cardProfileHeader.google.get())
        assertEquals(infoCardResponse.twitter, homeViewModel!!.cardProfileHeader.twitter.get())
        assertEquals(infoCardResponse.linkedin, homeViewModel!!.cardProfileHeader.linkedin.get())
        assertEquals(infoCardResponse.name, homeViewModel!!.cardProfileHeader.name.get())
        assertEquals(infoCardResponse.role?.sv, homeViewModel!!.cardProfileHeader.role.get())
    }

    @Test
    fun shouldCallHomeViewActionHandlerOnOpenExternalSite() {

        When calling view.tag itReturns "externalsiteurl"
        homeViewModel!!.openExternalSite(view)

        Verify on homeViewAction that homeViewAction.openExternalSite("externalsiteurl") was called

    }

    @Test
    fun shouldCallHomeViewActionHandlerOnSaveToStorage() {

        When calling view.tag itReturns "externalfileurl"
        homeViewModel!!.saveToStorage(view)

        Verify on homeViewAction that homeViewAction.saveToStorage(
            "externalfileurl",
            "se"
        ) was called

    }

    @Test
    fun shouldSetPreferenceAndLoadContentWhenSwedishLanguageIsSelected() {

        val spiedHomeViewModel = Mockito.spy(homeViewModel)
        spiedHomeViewModel!!.selectLangSV(view)

        Verify on sharedPreferencesService that sharedPreferencesService.putString(
            "language",
            "sv"
        ) was called
        verify(spiedHomeViewModel, times(1)).loadContent()

    }

    @Test
    fun shouldSetPreferenceAndLoadContentWhenEnglishLanguageIsSelected() {

        val spiedHomeViewModel = Mockito.spy(homeViewModel)
        spiedHomeViewModel!!.selectLangEN(view)

        Verify on sharedPreferencesService that sharedPreferencesService.putString(
            "language",
            "en"
        ) was called
        verify(spiedHomeViewModel, times(1)).loadContent()

    }

}