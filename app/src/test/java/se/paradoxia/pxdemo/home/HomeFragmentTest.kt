package se.paradoxia.pxdemo.home

import android.app.Fragment
import android.content.Context
import android.os.Build.VERSION_CODES.N
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import se.paradoxia.pxdemo.*
import se.paradoxia.pxdemo.di.ActivityContext
import se.paradoxia.pxdemo.home.di.HomeTestApp
import se.paradoxia.pxdemo.home.di.HomeTestAppComponent
import se.paradoxia.pxdemo.home.di.HomeTestAppModule
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.permission.PermissionViewModel
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.PermissionService
import se.paradoxia.pxdemo.service.RouterService
import javax.inject.Inject
import kotlin.test.assertEquals


// Due to problem with createConfigurationContext throwing exception with
// any Robolectric version below N (24), sdk is set to "N"

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = HomeTestApp::class, sdk = [N], qualifiers = "w360dp-h640dp-xhdpi")
class HomeFragmentTest : RobolectricTestBase() {

    val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
    val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

    var homeTestAppComponent: HomeTestAppComponent? = null

    private val localContentService = object : ContentService {
        override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
            return Observable.just(aboutMeResponse.toOptional())
        }

        override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
            return Observable.just(infoCardResponse.toOptional())
        }
    }

    private lateinit var activityController: ActivityController<StubMainActivity>
    private lateinit var mainActivity: MainActivity

    @Inject
    lateinit var spiedHomeViewModel: HomeViewModel

    @Before
    override fun setUp() {

        val app = RuntimeEnvironment.application as HomeTestApp
        val homeTestAppModule = HomeTestAppModule(localContentService)
        homeTestAppComponent = app.setModules(homeTestAppModule)
        homeTestAppComponent!!.inject(app)

        // HomeTestAppModule provides a mocked HomeViewModel singleton instance.
        // Let's inject the same instance to our Test
        injectProvidedSpiedHomeViewModel(homeTestAppComponent!!)

        activityController = Robolectric.buildActivity(StubMainActivity::class.java)
        mainActivity = activityController.setup().get()

    }

    private fun injectProvidedSpiedHomeViewModel(homeTestAppComponent: HomeTestAppComponent) {
        homeTestAppComponent.inject(this)
    }

    @Test
    fun shouldInitHomeViewModelAndAskForViewTypeMapsAndCards() {

        Verify on spiedHomeViewModel that spiedHomeViewModel.init(any(HomeLogic::class), any(String::class)) was called
        Verify on spiedHomeViewModel that spiedHomeViewModel.getViewTypeMap() was called
        Verify on spiedHomeViewModel that spiedHomeViewModel.getCards() was called

    }

    @Test
    fun shouldInjectFragmentInActivity() {

        val z = mainActivity.activeFragment
        println("x")

        //Verify on mainActivity that mainActivity.addFragmentToActivity(any(FragmentManager::class),any(HomeFragment::class), any(Int::class))
    }

    @Test
    fun shouldBeEnglishInProfileHeaderTextViewsByDefault() {

        val recyclerView: RecyclerView = mainActivity.findViewById(R.id.recViewHome)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val name = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvName).text.toString()
        val roleEn = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvRole).text.toString()
        val downloadEn = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btDownload).text.toString()

        assertEquals("Mikael Olsson", name)
        assertEquals("Solution Architect", roleEn)
        assertEquals("Download CV", downloadEn)

    }

    @Test
    fun shouldBeSwedishInProfileHeaderTextViewsAfterLanguageChange() {

        val recyclerView: RecyclerView = mainActivity.findViewById(R.id.recViewHome)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)
        val name = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvName).text.toString()

        viewHolderProfileHeader.itemView.findViewById<ImageView>(R.id.ivSelectSwedish).callOnClick()

        val roleSv = viewHolderProfileHeader.itemView.findViewById<TextView>(R.id.tvRole).text.toString()
        val downloadSv = viewHolderProfileHeader.itemView.findViewById<AppCompatButton>(R.id.btDownload).text.toString()

        assertEquals("Mikael Olsson", name)
        assertEquals("Systemarkitekt", roleSv)
        assertEquals("Ladda ned CV", downloadSv)

    }

    @Test
    fun shouldX() {

        val recyclerView: RecyclerView = mainActivity.findViewById(R.id.recViewHome)

        recyclerView.scrollToPosition(1)

        val viewHolderProfileHeader = recyclerView.findViewHolderForAdapterPosition(0)

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
        finishThreads()
    }

}

class StubMainActivity : MainActivity() {

    @Inject
    @field:ActivityContext
    lateinit var x: Context

    @Inject
    lateinit var routerService: RouterService


    override fun getDefaultFragment(): Fragment {

        val customPermissionService = object : PermissionService {
            override fun havePermission(activity: AppCompatActivity, permission: String, viewModel: PermissionViewModel?): Boolean {
                return true
            }

            override fun permissionToRequestCode(permission: String): Int {
                return 0
            }
        }

        val homeFragmentLogic = Mockito.spy(HomeLogic(customPermissionService))
        return HomeFragment.newInstance(homeFragmentLogic)

//        return super.getDefaultFragment()
        //return Mockito.spy(super.getDefaultFragment())
    }
}





