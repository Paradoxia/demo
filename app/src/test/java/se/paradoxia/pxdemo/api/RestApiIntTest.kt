package se.paradoxia.pxdemo.api

import io.reactivex.disposables.CompositeDisposable
import org.junit.Test
import se.paradoxia.pxdemo.Junit4TestBase
import kotlin.test.assertNotNull

/**
 * Created by mikael on 2018-02-03.
 */
class RestApiIntTest : Junit4TestBase() {

    private lateinit var restApi: RestApi

    private val disposables = CompositeDisposable()

    override fun setUp() {
        restApi = RestApi.create()
    }

    override fun tearDown() {
        disposables.clear()
    }

    @Test
    fun shouldGetAboutMeResponseFromServer() {

        disposables.add(restApi.getAboutMe().subscribe({ result ->
            assertNotNull(result.id)
            assertNotNull(result.aboutMeEn?.headline)
            assertNotNull(result.aboutMeEn?.text)
            assertNotNull(result.aboutMeEn?.title)
            assertNotNull(result.aboutMeSv?.headline)
            assertNotNull(result.aboutMeSv?.text)
            assertNotNull(result.aboutMeSv?.title)
        }))
    }

    @Test
    fun shouldGetInfoCardResponseFromServer() {

        disposables.add(restApi.getInfoCard().subscribe({ result ->
            assertNotNull(result.id)
            assertNotNull(result.downloadFile?.en)
            assertNotNull(result.downloadFile?.sv)
            assertNotNull(result.downloadText?.en)
            assertNotNull(result.downloadText?.sv)
            assertNotNull(result.facebook)
            assertNotNull(result.google)
            assertNotNull(result.instagram)
            assertNotNull(result.linkedin)
            assertNotNull(result.twitter)
            assertNotNull(result.name)
            assertNotNull(result.profileImage?.x1)
            assertNotNull(result.profileImage?.x2)
            assertNotNull(result.role?.en)
            assertNotNull(result.role?.sv)
        }))

    }

}