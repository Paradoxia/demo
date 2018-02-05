package se.paradoxia.pxdemo.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.N
import android.support.annotation.IdRes
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
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity
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
import se.paradoxia.pxdemo.util.Crc16
import kotlin.test.assertEquals


// Due to problem with createConfigurationContext throwing exception with
// any Robolectric version below N (24), sdk is set to "N"

@RunWith(RobolectricTestRunner::class)
@Config(
    constants = BuildConfig::class,
    application = HomeTestApp::class,
    sdk = [N],
    qualifiers = "w360dp-h640dp-xhdpi"
)
class HomeViewTest : RobolectricTestBase() {

    val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
    val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

    private var homeTestAppComponent: HomeTestAppComponent? = null
    private var activityController: ActivityController<MainActivity>? = null
    private var mainActivity: MainActivity? = null
    private var shadowMainActivity: ShadowActivity? = null

    private val localContentService = object : ContentService {
        override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
            return Observable.just(aboutMeResponse.toOptional())
        }

        override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
            return Observable.just(infoCardResponse.toOptional())
        }
    }

    companion object {
        fun permissionToRequestCode(permission: String): Int {
            return Crc16().crc16(permission.toByteArray())
        }
    }


    @AllOpen
    class CustomPermissionService : PermissionService {
        override fun havePermission(
            activity: AppCompatActivity,
            permission: String,
            viewModel: PermissionViewModel?
        ): Boolean {
            return true
        }

        override fun permissionToRequestCode(permission: String): Int {
            return HomeViewTest.permissionToRequestCode(permission)
        }
    }

    @Before
    override fun setUp() {

        val app = RuntimeEnvironment.application as HomeTestApp
        val customPermissionService = Mockito.spy(CustomPermissionService())
        val homeTestAppModule = HomeTestAppModule(localContentService, customPermissionService)

        homeTestAppComponent = app.setModules(homeTestAppModule)
        homeTestAppComponent!!.inject(app)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        mainActivity = activityController!!.setup().get()
        shadowMainActivity = Shadows.shadowOf(mainActivity)

    }

    @After
    override fun tearDown() {
        activityController!!.pause().stop().destroy()
        mainActivity!!.finish()
        finishThreads()
        activityController = null
        mainActivity = null
        homeTestAppComponent = null
        shadowMainActivity = null
    }

    private fun extractHomeViewModel(stubMainActivity: MainActivity?): HomeViewModel {
        return (stubMainActivity!!.activeFragment as HomeView).homeViewModel
    }

    private fun extractHomeViewLogic(stubMainActivity: MainActivity?): HomeViewLogic {
        return (stubMainActivity!!.activeFragment as HomeView).homeViewLogic
    }

    private fun extractPermissionService(stubMainActivity: MainActivity?): PermissionService {
        return ((stubMainActivity!!.activeFragment as HomeView).homeViewLogic as HomeViewLogicImpl).permissionService
    }

    @Test
    fun shouldInteractWithViewModelAndInitGetViewTypeMapAndGetCards() {

        val homeViewModel = extractHomeViewModel(mainActivity)

        Verify on homeViewModel that homeViewModel.init(
            any(HomeViewLogicImpl::class),
            any(String::class)
        ) was called
        Verify on homeViewModel that homeViewModel.getViewTypeMap() was called
        Verify on homeViewModel that homeViewModel.getCards() was called

    }

    @Test
    fun shouldBeEnglishInProfileHeaderTextViewsByDefault() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val profileHeaderName =
            viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderName)
                .text.toString()
        val profileHeaderRole =
            viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderRole)
                .text.toString()
        val profileHeaderDownloadCV =
            viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btProfileHeaderDownload)
                .text.toString()

        assertEquals(infoCardResponse.name, profileHeaderName)
        assertEquals(infoCardResponse.role!!.en, profileHeaderRole)
        assertEquals(infoCardResponse.downloadText!!.en, profileHeaderDownloadCV)

    }

    @Test
    fun shouldBeEnglishInAboutMeTextViewsByDefault() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)

        // Scroll to "second" card
        recyclerView.scrollToPosition(1)
        val viewHolderAboutMe = recyclerView.findViewHolderForAdapterPosition(1)

        val aboutMeText =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeText).text.toString()
        val aboutMeHeadline =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeHeadline)
                .text.toString()
        val aboutMeTitle =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeTitle).text.toString()

        assertEquals(aboutMeResponse.aboutMeEn!!.text, aboutMeText)
        assertEquals(aboutMeResponse.aboutMeEn!!.headline, aboutMeHeadline)
        assertEquals(aboutMeResponse.aboutMeEn!!.title, aboutMeTitle)

    }


    @Test
    fun shouldBeSwedishInProfileHeaderTextViewsAfterLanguageChange() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)

        // Scroll to "second" card
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val profileHeaderName =
            viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderName)
                .text.toString()

        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()

        Verify on extractHomeViewModel(mainActivity) that extractHomeViewModel(mainActivity).selectLangSV(
            any(View::class)
        ) was called

        val profileHeaderRole =
            viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvProfileHeaderRole)
                .text.toString()
        val profileHeaderDownloadCV =
            viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.btProfileHeaderDownload)
                .text.toString()

        assertEquals(infoCardResponse.name, profileHeaderName)
        assertEquals(infoCardResponse.role!!.sv, profileHeaderRole)
        assertEquals(infoCardResponse.downloadText!!.sv, profileHeaderDownloadCV)


    }

    @Test
    fun shouldBeSwedishInAboutMeTextViewsByDefault() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()

        // Scroll to "second" card
        recyclerView.scrollToPosition(1)
        val viewHolderAboutMe = recyclerView.findViewHolderForAdapterPosition(1)

        val aboutMeText =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeText).text.toString()
        val aboutMeHeadline =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeHeadline)
                .text.toString()
        val aboutMeTitle =
            viewHolderAboutMe.itemView.findViewById<TextView>(R.id.tvAboutMeTitle).text.toString()

        assertEquals(aboutMeResponse.aboutMeSv!!.text, aboutMeText)
        assertEquals(aboutMeResponse.aboutMeSv!!.headline, aboutMeHeadline)
        assertEquals(aboutMeResponse.aboutMeSv!!.title, aboutMeTitle)

    }

    @Test
    fun shouldTriggerPermissionCheckAndDownloadWhen() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val downloadBtn =
            viewHolderProfileHeader.itemView.findViewById<View>(R.id.btProfileHeaderDownload)
        downloadBtn.callOnClick()

        val homeViewLogic = extractHomeViewLogic(mainActivity)

        Verify on extractHomeViewModel(mainActivity) that extractHomeViewModel(mainActivity).saveToStorage(
            any(View::class)
        ) was called

        Verify on homeViewLogic that homeViewLogic.saveToStorage(
            eq(BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile!!.en!!),
            eq("en")
        ) was called

        Verify on homeViewLogic that homeViewLogic.getLocalizedResources(
            any(MainActivity::class),
            eq("en")
        ) was called

        Verify on homeViewLogic that homeViewLogic.download(eq(BuildConfig.FILE_BASE_URL + infoCardResponse.downloadFile!!.en!!)) was called

        val permissionService = extractPermissionService(mainActivity)

        Verify on permissionService that permissionService.havePermission(
            any(MainActivity::class),
            eq("android.permission.WRITE_EXTERNAL_STORAGE"),
            any(PermissionViewModel::class)
        ) was called


    }

    @Test
    fun shouldTriggerOpenExternalSiteWhenFacebookButtonIsPressed() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

        val faceBookUrl =
            pressViewButtonAndCaptureUrlFromViewTag(R.id.ivFacebook, viewHolderProfileHeader)
        val expectedFacebookUrl = "fb://facewebmodal/f?href=${infoCardResponse.facebook}"
        assertEquals(expectedFacebookUrl, faceBookUrl)

        val homeViewLogic = extractHomeViewLogic(mainActivity)
        Verify on homeViewLogic that homeViewLogic.openExternalSite(eq(expectedFacebookUrl)) was called
        val intent = shadowMainActivity!!.peekNextStartedActivity()
        assertEquals(intent.action, Intent.ACTION_VIEW)
        assertEquals(intent.data.toString(), expectedFacebookUrl)

    }

    @Test
    fun shouldTriggerOpenExternalSiteWhenInstagramIsPressed() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

        val instagramUrl = pressViewButtonAndCaptureUrlFromViewTag(
            R.id.ivInstagram,
            viewHolderProfileHeader
        )
        val expectedInstagramUrl = infoCardResponse.instagram
        assertEquals(expectedInstagramUrl, instagramUrl)

        val homeViewLogic = extractHomeViewLogic(mainActivity)
        Verify on homeViewLogic that homeViewLogic.openExternalSite(eq(expectedInstagramUrl!!)) was called
        val intent = shadowMainActivity!!.peekNextStartedActivity()
        assertEquals(intent.action, Intent.ACTION_VIEW)
        assertEquals(intent.data.toString(), expectedInstagramUrl)


    }

    @Test
    fun shouldTriggerOpenExternalSiteWhenGooglePlusIsPressed() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

        val googlePlusUrl = pressViewButtonAndCaptureUrlFromViewTag(
            R.id.ivGooglePlus,
            viewHolderProfileHeader
        )
        val expectedGooglePlusUrl = infoCardResponse.google
        assertEquals(expectedGooglePlusUrl, googlePlusUrl)

        val homeViewLogic = extractHomeViewLogic(mainActivity)
        Verify on homeViewLogic that homeViewLogic.openExternalSite(eq(expectedGooglePlusUrl!!)) was called
        val intent = shadowMainActivity!!.peekNextStartedActivity()
        assertEquals(intent.action, Intent.ACTION_VIEW)
        assertEquals(intent.data.toString(), expectedGooglePlusUrl)


    }

    @Test
    fun shouldTriggerOpenExternalSiteWhenLinkedInIsPressed() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

        val linkedInUrl = pressViewButtonAndCaptureUrlFromViewTag(
            R.id.ivLinkedIn,
            viewHolderProfileHeader
        )
        val expectedLinkedInUrl = infoCardResponse.linkedin
        assertEquals(expectedLinkedInUrl, linkedInUrl)

        val homeViewLogic = extractHomeViewLogic(mainActivity)
        Verify on homeViewLogic that homeViewLogic.openExternalSite(eq(expectedLinkedInUrl!!)) was called
        val intent = shadowMainActivity!!.peekNextStartedActivity()
        assertEquals(intent.action, Intent.ACTION_VIEW)
        assertEquals(intent.data.toString(), expectedLinkedInUrl)

    }

    @Test
    fun shouldTriggerOpenExternalSiteWhenTwitterIsPressed() {

        val recyclerView: RecyclerView = mainActivity!!.findViewById(R.id.recViewHome)
        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

        val twitterUrl = pressViewButtonAndCaptureUrlFromViewTag(
            R.id.ivTwitter,
            viewHolderProfileHeader
        )
        val expectedTwitterUrl = infoCardResponse.twitter
        assertEquals(expectedTwitterUrl, twitterUrl)

        val homeViewLogic = extractHomeViewLogic(mainActivity)
        Verify on homeViewLogic that homeViewLogic.openExternalSite(eq(expectedTwitterUrl!!)) was called
        val intent = shadowMainActivity!!.peekNextStartedActivity()
        assertEquals(intent.action, Intent.ACTION_VIEW)
        assertEquals(intent.data.toString(), expectedTwitterUrl)

    }

    @Test
    fun shouldTriggerDownloadWhenPermissionIsGranted() {

        val homeViewLogic = extractHomeViewLogic(mainActivity)

        (homeViewLogic as HomeViewLogicImpl).saveToStorageUrl = BuildConfig.FILE_BASE_URL +
                infoCardResponse.downloadFile!!.en!!

        val requestCode = permissionToRequestCode(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        homeViewLogic.onRequestPermissionsResult(
            requestCode, listOf(
                Manifest.permission
                    .WRITE_EXTERNAL_STORAGE
            ).toTypedArray(), listOf(
                PackageManager
                    .PERMISSION_GRANTED
            ).toIntArray()
        )

        Verify on homeViewLogic that homeViewLogic.download(
            eq(
                BuildConfig.FILE_BASE_URL +
                        infoCardResponse.downloadFile!!.en!!
            )
        ) was called


    }

    private fun pressViewButtonAndCaptureUrlFromViewTag(@IdRes resId: Int, viewHolder: RecyclerView.ViewHolder): String {

        val viewButton = viewHolder.itemView.findViewById<ImageView>(resId)
        viewButton.callOnClick()

        val argumentCaptor = ArgumentCaptor.forClass(View::class.java)
        Verify on extractHomeViewModel(mainActivity) that extractHomeViewModel(mainActivity)
            .openExternalSite(argumentCaptor.capture()) was called

        return argumentCaptor.value.tag as String

    }

}



