package se.paradoxia.pxdemo.home

import android.app.Fragment
import android.os.Build.VERSION_CODES.N
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.nhaarman.mockito_kotlin.eq
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mockingDetails
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import se.paradoxia.pxdemo.*
import se.paradoxia.pxdemo.home.di.HomeTestApp
import se.paradoxia.pxdemo.home.di.HomeTestAppComponent
import se.paradoxia.pxdemo.home.di.HomeTestAppModule
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.home.view.HomeView
import se.paradoxia.pxdemo.home.view.HomeViewLogic
import se.paradoxia.pxdemo.home.view.HomeViewLogicImpl
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.permission.PermissionViewModel
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.PermissionService
import se.paradoxia.pxdemo.util.AllOpen
import javax.inject.Inject
import kotlin.test.assertEquals

// Due to problem with createConfigurationContext throwing exception with
// any Robolectric version below N (24), sdk is set to "N"

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = HomeTestApp::class, sdk = [N], qualifiers = "w360dp-h640dp-xhdpi")
class HomeViewTest : RobolectricTestBase() {

    val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
    val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

    private var homeTestAppComponent: HomeTestAppComponent? = null
    private var activityController: ActivityController<StubMainActivity>? = null
    private var stubMainActivity: StubMainActivity? = null

    private val localContentService = object : ContentService {
        override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
            return Observable.just(aboutMeResponse.toOptional())
        }

        override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
            return Observable.just(infoCardResponse.toOptional())
        }
    }

    @AllOpen
    class CustomPermissionService : PermissionService {
        override fun havePermission(activity: AppCompatActivity, permission: String, viewModel: PermissionViewModel?): Boolean {
            return true
        }

        override fun permissionToRequestCode(permission: String): Int {
            return 0
        }
    }

    @Before
    override fun setUp() {

        val app = RuntimeEnvironment.application as HomeTestApp
        val customPermissionService = Mockito.spy(CustomPermissionService())
        val homeTestAppModule = HomeTestAppModule(localContentService, customPermissionService)

        println("Created $customPermissionService")

        homeTestAppComponent = app.setModules(homeTestAppModule)
        homeTestAppComponent!!.inject(app)

        activityController = Robolectric.buildActivity(StubMainActivity::class.java)
        stubMainActivity = activityController!!.setup().get()

    }

    @After
    override fun tearDown() {
        activityController!!.pause().stop().destroy()
        stubMainActivity!!.finish()
        finishThreads()
        activityController = null
        stubMainActivity = null
        homeTestAppComponent = null
    }

    private fun extractHomeViewModel(stubMainActivity : StubMainActivity?) : HomeViewModel {
        return stubMainActivity!!.spiedHomeViewModel
    }

    private fun extractHomeViewLogic(stubMainActivity : StubMainActivity?) : HomeViewLogic {
        return (stubMainActivity!!.activeFragment as HomeView).homeViewLogic
    }

    private fun extractPermissionServiceFromHomeViewLogic(stubMainActivity : StubMainActivity?) : PermissionService {
        return ((stubMainActivity!!.activeFragment as HomeView).homeViewLogic as HomeViewLogicImpl).permissionService
    }

    @Test
    fun shouldInteractWithViewModelAndInitGetViewTypeMapAndGetCards() {

        val homeViewModel = extractHomeViewModel(stubMainActivity)

        Verify on homeViewModel that homeViewModel.init(any(HomeViewLogicImpl::class), any(String::class)) was called
        Verify on homeViewModel that homeViewModel.getViewTypeMap() was called
        Verify on homeViewModel that homeViewModel.getCards() was called

    }

    @Test
    fun shouldBeEnglishInProfileHeaderTextViewsByDefault() {

        val recyclerView: RecyclerView = stubMainActivity!!.findViewById(R.id.recViewHome)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val profileHeaderName = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderName).text.toString()
        val profileHeaderRole = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderRole).text.toString()
        val profileHeaderDownloadCV = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btProfileHeaderDownload).text.toString()

        assertEquals(infoCardResponse.name, profileHeaderName)
        assertEquals(infoCardResponse.role!!.en, profileHeaderRole)
        assertEquals(infoCardResponse.downloadText!!.en, profileHeaderDownloadCV)

    }

    @Test
    fun shouldBeEnglishInAboutMeTextViewsByDefault() {

        val recyclerView: RecyclerView = stubMainActivity!!.findViewById(R.id.recViewHome)

        // Scroll to "second" card
        recyclerView.scrollToPosition(1)
        val viewHolderAboutMe = recyclerView.findViewHolderForAdapterPosition(1)

        val aboutMeText = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeText).text.toString()
        val aboutMeHeadline = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeHeadline).text.toString()
        val aboutMeTitle = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeTitle).text.toString()

        assertEquals(aboutMeResponse.aboutMeEn!!.text, aboutMeText)
        assertEquals(aboutMeResponse.aboutMeEn!!.headline, aboutMeHeadline)
        assertEquals(aboutMeResponse.aboutMeEn!!.title, aboutMeTitle)

    }


    @Test
    fun shouldBeSwedishInProfileHeaderTextViewsAfterLanguageChange() {

        val recyclerView: RecyclerView = stubMainActivity!!.findViewById(R.id.recViewHome)

        // Scroll to "second" card
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val profileHeaderName = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderName).text.toString()

        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()

        Verify on extractHomeViewModel(stubMainActivity) that extractHomeViewModel(stubMainActivity).selectLangSV(any(View::class)) was called

        val profileHeaderRole = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderRole).text.toString()
        val profileHeaderDownloadCV = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.btProfileHeaderDownload).text.toString()

        assertEquals(infoCardResponse.name, profileHeaderName)
        assertEquals(infoCardResponse.role!!.sv, profileHeaderRole)
        assertEquals(infoCardResponse.downloadText!!.sv, profileHeaderDownloadCV)


    }

    @Test
    fun shouldBeSwedishInAboutMeTextViewsByDefault() {

        val recyclerView: RecyclerView = stubMainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()

        // Scroll to "second" card
        recyclerView.scrollToPosition(1)
        val viewHolderAboutMe = recyclerView.findViewHolderForAdapterPosition(1)

        val aboutMeText = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeText).text.toString()
        val aboutMeHeadline = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeHeadline).text.toString()
        val aboutMeTitle = viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeTitle).text.toString()

        assertEquals(aboutMeResponse.aboutMeSv!!.text, aboutMeText)
        assertEquals(aboutMeResponse.aboutMeSv!!.headline, aboutMeHeadline)
        assertEquals(aboutMeResponse.aboutMeSv!!.title, aboutMeTitle)

    }

    @Test
    fun shouldTriggerPermissionCheckAndDownloadWhen() {

        val recyclerView: RecyclerView = stubMainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val downloadBtn = viewHolderProfileHeader.itemView.findViewById<View>(R.id.btProfileHeaderDownload)
        downloadBtn.callOnClick()

        //System.out.println(mockingDetails(stubMainActivity.spiedHomeViewLogic).printInvocations())

        val homeViewLogic = extractHomeViewLogic(stubMainActivity)

        Verify on extractHomeViewModel(stubMainActivity) that extractHomeViewModel(stubMainActivity).saveToStorage(any(View::class)) was called

        Verify on homeViewLogic that homeViewLogic.saveToStorage(eq(BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile!!.en!!), eq("en")) was called
        Verify on homeViewLogic that homeViewLogic.getLocalizedResources(any(StubMainActivity::class), eq("en")) was called
        Verify on homeViewLogic that homeViewLogic.download(eq(BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile!!.en!!)) was called


        val permissionService = extractPermissionServiceFromHomeViewLogic(stubMainActivity)
        System.out.println(mockingDetails(permissionService).printInvocations())

        Verify on permissionService that permissionService.havePermission(any(StubMainActivity::class), eq("android.permission.WRITE_EXTERNAL_STORAGE"), any(PermissionViewModel::class)) was called


    }

}

class StubMainActivity : MainActivity() {

    @Inject
    lateinit var spiedHomeViewLogic: HomeViewLogic

    @Inject
    lateinit var spiedHomeViewModel: HomeViewModel

    override fun getDefaultFragment(): Fragment {

        return HomeView.newInstance()

    }

}





