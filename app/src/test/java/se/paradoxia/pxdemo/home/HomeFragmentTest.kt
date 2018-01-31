package se.paradoxia.pxdemo.home

import android.os.Build.VERSION_CODES.N
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.atLeast
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.RobolectricTestBase
import se.paradoxia.pxdemo.home.di.HomeTestApp
import se.paradoxia.pxdemo.home.di.HomeTestAppModule
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import javax.inject.Inject
import kotlin.test.assertEquals

// Due to problem with createConfigurationContext throwing exception with
// any Robolectric version below N (24), sdk is set to "N"

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = HomeTestApp::class, sdk = [N], qualifiers = "w360dp-h640dp-xhdpi")
class HomeFragmentTest : RobolectricTestBase() {

    private lateinit var activityController: ActivityController<MainActivity>
    private lateinit var mainActivity: MainActivity

    @Inject
    lateinit var spiedHomeViewModel: HomeViewModel

    @Before
    override fun setUp() {
        val app = RuntimeEnvironment.application as HomeTestApp
        val homeTestAppModule = HomeTestAppModule(localContentService)
        val homeTestAppComponent = app.setModules(homeTestAppModule)
        homeTestAppComponent.inject(app)
        homeTestAppComponent.inject(this)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        mainActivity = activityController.create().postCreate(null).start().resume().visible().get()
    }

    @Test
    fun shouldX() {

        Verify on spiedHomeViewModel that spiedHomeViewModel.init(any(HomeFragment::class), any(String::class)) was called
        verify(spiedHomeViewModel).homeViewAction = mainActivity.activeFragment as HomeViewAction

        verify(spiedHomeViewModel, atLeast(1)).cardProfileHeader

        Verify on spiedHomeViewModel that spiedHomeViewModel.loadContent() was called

        verify(spiedHomeViewModel).language = "en"

        verify(spiedHomeViewModel, atLeast(1)).cardAboutMe

        val recyclerView: RecyclerView = mainActivity.findViewById(R.id.recViewHome)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val name = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvName).text.toString()
        val roleEn = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvRole).text.toString()
        val downloadEn = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btDownload).text.toString()

        assertEquals("Mikael Olsson", name)
        assertEquals("Solution Architect", roleEn)
        assertEquals("Download CV", downloadEn)

        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()
        val roleSv = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvRole).text.toString()
        val downloadSv = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btDownload).text.toString()

        assertEquals("Systemarkitekt", roleSv)
        assertEquals("Ladda ned CV", downloadSv)

        recyclerView.scrollToPosition(1)

        val downloadBtn = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btDownload)
        downloadBtn.callOnClick()

        Verify on spiedHomeViewModel that spiedHomeViewModel.saveToStorage(any(View::class)) was called

        val firstCount = recyclerView.childCount
        recyclerView.scrollToPosition(1)
        val secondCount = recyclerView.childCount
        val itemCount = recyclerView.adapter.itemCount



        println("z")
    }

    @After
    override fun tearDown() {
        activityController.pause().stop().destroy()
        mainActivity.finish()
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

