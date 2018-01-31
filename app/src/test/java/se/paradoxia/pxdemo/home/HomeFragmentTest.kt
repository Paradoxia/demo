package se.paradoxia.pxdemo.home

import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.home.di.HomeTestApp
import se.paradoxia.pxdemo.home.di.HomeTestAppModule
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService


/**
 * Created by mikael on 2018-01-30.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = HomeTestApp::class, qualifiers = "w360dp-h640dp-xhdpi")
class HomeFragmentTest {

    private lateinit var mainActivity: MainActivity

    @Before
    fun setup() {
        val app = RuntimeEnvironment.application as HomeTestApp
        val homeTestAppModule = HomeTestAppModule(localContentService)
        app.setModules(homeTestAppModule)
        mainActivity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun shouldX() {
        val z: FrameLayout = mainActivity.findViewById(R.id.flPage)
        val recyclerView: RecyclerView = z.findViewById(R.id.recViewHome)
        val firstCount = recyclerView.childCount
        recyclerView.scrollToPosition(1)
        val secondCount = recyclerView.childCount
        val itemCount = recyclerView.adapter.itemCount
        println("z")
    }

}

val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

val localContentService = object : ContentService {
    override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
        return Observable.just(aboutMeResponse.toOptional())
    }

    override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
        return Observable.just(infoCardResponse.toOptional())
    }
}

/**
 * sourceSets defined to include "src/main/res/raw" in tests
 */
private inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
    val textAsJson = HomeFragmentTest::class.java.classLoader.getResource(rawFileName).readText()
    return Gson().fromJson(textAsJson, E::class.java)
}

