package se.paradoxia.pxdemo.home

import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
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
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import javax.inject.Singleton


/**
 * Created by mikael on 2018-01-30.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = HomeTestApp::class, qualifiers = "w360dp-h640dp-xhdpi")
class HomeFragmentTest {

    private lateinit var mainActivity: MainActivity

    //https://stackoverflow.com/questions/26939340/how-do-you-override-a-module-dependency-in-a-unit-test-with-dagger-2-0

    @Before
    fun setup() {
//        mainActivity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun shouldX() {


        val app = RuntimeEnvironment.application as HomeTestApp

        val homeTestAppModule = HomeTestAppModule(localContentService)


        app.setModules(homeTestAppModule, TestViewModelModule())
        mainActivity = Robolectric.setupActivity(MainActivity::class.java)


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
    val textAsJson = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
    return Gson().fromJson(textAsJson, E::class.java)
}

@Module
class TestViewModelModule : HomeTestViewModelModuleInterface {

    override fun homeViewModel(contentService: ContentService, sharedPreferencesService: SharedPreferencesService): HomeViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    @Provides
    @Singleton
    fun provideContentService(): ContentService {
        return localContentService
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesService(): SharedPreferencesService {
        return object : SharedPreferencesService {
            override fun getString(key: String, groupKey: String?, defaultValue: String?): String? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getBoolean(key: String, groupKey: String?, defaultValue: Boolean?): Boolean? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getInt(key: String, groupKey: String?, defaultValue: Int?): Int? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun putString(key: String, value: String?, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun putBoolean(key: String, value: Boolean?, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun putInt(key: String, value: Int?, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun removeString(key: String, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun removeBoolean(key: String, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun removeInt(key: String, groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun removeAll(groupKey: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    /**
     * sourceSets defined to include "src/main/res/raw" in tests
     */
    private inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
        val textAsJson = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
        return Gson().fromJson(textAsJson, E::class.java)
    }
}
