package se.paradoxia.pxdemo.home

import android.view.View
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
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
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.di.App
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.ViewTypeMapper
import kotlin.test.assertEquals

/**
 * Created by mikael on 2018-01-29.
 */
class HomeViewModelTest {

    private val app: App = Mockito.mock(App::class.java)
    private val contentService: ContentService = Mockito.mock(ContentService::class.java)
    private val sharedPreferencesService: SharedPreferencesService = Mockito.mock(SharedPreferencesService::class.java)
    private val homeViewAction: HomeViewAction = Mockito.mock(HomeViewAction::class.java)
    private val testScheduler = TestScheduler()
    private val view: View = Mockito.mock(View::class.java)

    @Test
    fun shouldLoadContentAndUpdateObservableFields() {

        val homeViewModel = HomeViewModel(contentService, sharedPreferencesService)
        val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
        val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

        When calling contentService.fetchAboutMe() itReturns Observable.just(aboutMeResponse.toOptional())
        When calling contentService.fetchInfoCard() itReturns Observable.just(infoCardResponse.toOptional()).observeOn(testScheduler)
        When calling sharedPreferencesService.getString(any(), anyOrNull(), any()) itReturns "sv"

        val expectedCards = listOf(HomeViewModel.CardProfileHeader(homeViewModel), HomeViewModel.CardAboutMe())
        val actualCards = homeViewModel.getCards()
        expectedCards.forEachIndexed { index, expectedCard ->
            assertEquals(expectedCard::class.java, actualCards[index]::class.java)
        }

        val expectedViewTypeMaps = listOf(
                ViewTypeMapper(HomeViewModel.CardAboutMe::class.java,
                        R.layout.card_about_me,
                        CardAboutMeViewHolder::class.java),
                ViewTypeMapper(HomeViewModel.CardProfileHeader::class.java,
                        R.layout.card_profile_header,
                        CardProfileHeaderViewHolder::class.java))
        val actualViewTypMaps = homeViewModel.getViewTypeMap()

        expectedViewTypeMaps.forEachIndexed { index, expectedViewTypeMap ->
            MatcherAssert.assertThat(actualViewTypMaps[index],
                    SamePropertyValuesAs.samePropertyValuesAs(expectedViewTypeMap))
        }


        homeViewModel.init(homeViewAction, "profileimageresourceurl")

        assertEquals("profileimageresourceurl", homeViewModel.cardProfileHeader.profileImage.get())
        testScheduler.triggerActions()

        assertEquals(aboutMeResponse.aboutMeSv?.text, homeViewModel.cardAboutMe.aboutMeText.get())
        assertEquals(aboutMeResponse.aboutMeSv?.title, homeViewModel.cardAboutMe.aboutMeTitle.get())
        assertEquals(aboutMeResponse.aboutMeSv?.headline, homeViewModel.cardAboutMe.aboutMeHeadline.get())

        assertEquals(BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile?.sv,
                homeViewModel.cardProfileHeader.downloadFile.get())

        assertEquals(infoCardResponse.downloadText?.sv, homeViewModel.cardProfileHeader.downloadText.get())

        assertEquals(BuildConfig.FACEBOOK_EXTERNAL_URL + infoCardResponse.facebook, homeViewModel.cardProfileHeader.facebook.get())

        assertEquals(infoCardResponse.instagram, homeViewModel.cardProfileHeader.instagram.get())
        assertEquals(infoCardResponse.google, homeViewModel.cardProfileHeader.google.get())
        assertEquals(infoCardResponse.twitter, homeViewModel.cardProfileHeader.twitter.get())
        assertEquals(infoCardResponse.linkedin, homeViewModel.cardProfileHeader.linkedin.get())
        assertEquals(infoCardResponse.name, homeViewModel.cardProfileHeader.name.get())

        assertEquals(BuildConfig.IMAGE_BASE_URL + infoCardResponse.profileImage?.x2,
                homeViewModel.cardProfileHeader.profileImage.get())

        assertEquals(infoCardResponse.role?.sv, homeViewModel.cardProfileHeader.role.get())

        When calling view.tag itReturns "externalsiteurl"
        homeViewModel.openExternalSite(view)
        Verify on homeViewAction that homeViewAction.openExternalSite("externalsiteurl") was called

        When calling view.tag itReturns "externalfileurl"
        homeViewModel.saveToStorage(view)
        Verify on homeViewAction that homeViewAction.saveToStorage("externalfileurl","se") was called

        val spiedHomeViewModel = Mockito.spy(homeViewModel)
        spiedHomeViewModel.selectLangSV(view)
        Verify on sharedPreferencesService that sharedPreferencesService.putString("language","sv") was called

        spiedHomeViewModel.selectLangEN(view)
        Verify on sharedPreferencesService that sharedPreferencesService.putString("language","en") was called

        verify(spiedHomeViewModel, times(2)).loadContent()

    }


    /**
     * sourceSets defined to include "src/main/res/raw" in tests
     */
    private inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
        val textAsJson = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
        return Gson().fromJson(textAsJson, E::class.java)
    }


}