package se.paradoxia.pxdemo

import android.support.test.InstrumentationRegistry
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.provider.RawResourceProvider
import se.paradoxia.pxdemo.realm.RealmInstanceMaker
import se.paradoxia.pxdemo.realm.RealmProvider
import java.io.File


/**
 * Created by mikael on 2018-02-04.
 */
class RealmServiceTest {

    @get:Rule
    private val testFolder = TemporaryFolder()

    class InMemoryRealmInstanceMaker(private val tempFolder : File) : RealmInstanceMaker() {
        override fun getRealmInstance(): Realm {
            val testConfig = RealmConfiguration.Builder().directory(tempFolder).name("test-realm").build()
            return Realm.getInstance(testConfig)
        }
    }

    @Test
    fun shouldXYZ() {

        val tempFolder = testFolder.newFolder("realmdata")

        val context = InstrumentationRegistry.getTargetContext()
        Realm.init(context)
        val realmService = RealmProvider(InMemoryRealmInstanceMaker(tempFolder))
        val aboutMe = realmService.fetchAboutMe()

        val rawResourceService = RawResourceProvider(context)

        val aboutMeResponse = rawResourceService.readJson(R.raw.aboutmeresponse, AboutMeResponse::class.java)
        val x = realmService.saveAboutMe(aboutMeResponse)

        val aboutMe2 = realmService.fetchAboutMe()





        println("x")

    }

}