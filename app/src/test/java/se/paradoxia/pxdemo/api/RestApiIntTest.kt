package se.paradoxia.pxdemo.api

import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertNotNull
import org.junit.Test
import se.paradoxia.pxdemo.Junit4TestBase

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

    @Test
    fun shouldGetPersonalInfoResponseFromServer() {

        disposables.add(restApi.getPersonalInfo().subscribe({ result ->
            assertNotNull(result.id)
            assertNotNull(result.personalInfoEn?.dateOfBirth)
            assertNotNull(result.personalInfoEn?.email)
            assertNotNull(result.personalInfoEn?.fullName)
            assertNotNull(result.personalInfoEn?.languages?.get(0)?.title)
            assertNotNull(result.personalInfoEn?.languages?.get(0)?.value)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.dateOfBirth)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.email)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.fullName)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.langTitle)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.phone)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.piTitle)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.residence)
            assertNotNull(result.personalInfoEn?.personalInfoFields?.status)
            assertNotNull(result.personalInfoEn?.phone)
            assertNotNull(result.personalInfoEn?.residence)
            assertNotNull(result.personalInfoEn?.status)
            assertNotNull(result.personalInfoSv?.dateOfBirth)
            assertNotNull(result.personalInfoSv?.email)
            assertNotNull(result.personalInfoSv?.fullName)
            assertNotNull(result.personalInfoSv?.languages?.get(0)?.title)
            assertNotNull(result.personalInfoSv?.languages?.get(0)?.value)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.dateOfBirth)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.email)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.fullName)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.langTitle)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.phone)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.piTitle)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.residence)
            assertNotNull(result.personalInfoSv?.personalInfoFields?.status)
            assertNotNull(result.personalInfoSv?.phone)
            assertNotNull(result.personalInfoSv?.residence)
            assertNotNull(result.personalInfoSv?.status)
        }))
    }

}