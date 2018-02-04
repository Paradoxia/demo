package se.paradoxia.pxdemo

import android.content.Context
import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.provider.RawResourceProvider
import se.paradoxia.pxdemo.realm.RealmInstanceMaker
import se.paradoxia.pxdemo.realm.RealmProvider
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.RealmService
import java.io.File

/**
 * Created by mikael on 2018-02-04.
 */
@RunWith(AndroidJUnit4::class)
class RealmServiceTest {

    @get:Rule
    private val testFolder = TemporaryFolder()

    lateinit var realmService: RealmService
    lateinit var rawResourceService: RawResourceService

    lateinit var context: Context

    class TempFolderRealmInstanceMaker(private val tempFolder: File) : RealmInstanceMaker() {
        override fun getRealmInstance(): Realm {
            val testConfig = RealmConfiguration.Builder().directory(tempFolder).name("realm-test").build()
            return Realm.getInstance(testConfig)
        }
    }

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getTargetContext()
        testFolder.create()
        val tempFolder = testFolder.newFolder("realm-data")
        Realm.init(context)
        realmService = RealmProvider(TempFolderRealmInstanceMaker(tempFolder))
        rawResourceService = RawResourceProvider(context)
    }

    @After
    fun tearDown() {
        testFolder.delete()
    }

    @Test
    fun shouldFetchEmptyAboutMeResponse() {
        val response = realmService.fetchAboutMe()
        assertNull(response)
    }

    @Test
    fun shouldSaveAboutMeResponseAndFetchIt() {

        val response: AboutMeResponse = saveToRealmAndFetchAgain(
                realmService::fetchAboutMe,
                realmService::saveAboutMe,
                rawResourceService,
                R.raw.aboutmeresponse)
        assertNotNull(response)
    }

    @Test
    fun shouldFetchEmptyInfoCardResponse() {
        val response = realmService.fetchInfoCard()
        assertNull(response)
    }

    @Test
    fun shouldSaveInfoCardResponseAndFetchIt() {

        val response: InfoCardResponse = saveToRealmAndFetchAgain(
                realmService::fetchInfoCard,
                realmService::saveInfoCard,
                rawResourceService,
                R.raw.infocardresponse)
        assertNotNull(response)
    }


}

inline fun <reified E> saveToRealmAndFetchAgain(crossinline realmFetcher: () -> RealmObject?,
                                                crossinline realmSaver: (obj: RealmObject) -> Unit,
                                                rawResourceService: RawResourceService,
                                                @IdRes resId: Int): E {

    val localResourcesInstance = rawResourceService.readJson(resId, E::class.java) as RealmObject
    realmSaver(localResourcesInstance)
    val instanceFromRealm = realmFetcher()
    return instanceFromRealm as E
}
