package se.paradoxia.pxdemo.home

import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import io.reactivex.Observable
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.junit.Test
import org.mockito.Mockito
import se.paradoxia.pxdemo.di.App
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import kotlin.test.assertEquals

/**
 * Created by mikael on 2018-01-29.
 */
class HomeViewModelTest {

    private val app: App = Mockito.mock(App::class.java)
    private val contentService: ContentService = Mockito.mock(ContentService::class.java)
    private val sharedPreferencesService: SharedPreferencesService = Mockito.mock(SharedPreferencesService::class.java)

    @Test
    fun shouldX() {

        val homeViewModel = HomeViewModel(contentService, sharedPreferencesService)
        val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
        When calling contentService.fetchAboutMe() itReturns Observable.just(aboutMeResponse.toOptional())
        homeViewModel.loadContent()

        assertEquals(aboutMeResponse.aboutMeSv?.text, homeViewModel.cardAboutMe.aboutMeText.get())
        assertEquals(aboutMeResponse.aboutMeSv?.title, homeViewModel.cardAboutMe.aboutMeTitle.get())
        assertEquals(aboutMeResponse.aboutMeSv?.headline, homeViewModel.cardAboutMe.aboutMeHeadline.get())

    }

    /**
     * sourceSets defined to include "src/main/res/raw" in tests
     */
    private inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
        val z = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
        return Gson().fromJson(z, E::class.java)
    }


}