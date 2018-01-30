package se.paradoxia.pxdemo.di

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import se.paradoxia.pxdemo.home.HomeViewModel
import se.paradoxia.pxdemo.home.HomeViewModelTest
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService

/**
 * Created by mikael on 2018-01-30.
 */
@Module
class TestViewModelModule {

    val aboutMeResponse: AboutMeResponse = rawResourceToInstance("aboutmeresponse.json")
    val infoCardResponse: InfoCardResponse = rawResourceToInstance("infocardresponse.json")

    @Provides
    fun homeViewModel() : HomeViewModel {

        val contentService = object:ContentService {
            override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
                return Observable.just(aboutMeResponse.toOptional())
            }

            override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
                return Observable.just(infoCardResponse.toOptional())
            }
        }

        val sharedPreferencesService = object:SharedPreferencesService {
            override fun getString(key: String, groupKey: String?, defaultValue: String?): String? {
                if(key == "language") {
                    return "sv"
                } else {
                    return "en"
                }
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

        return HomeViewModel(contentService, sharedPreferencesService)
    }

    /**
     * sourceSets defined to include "src/main/res/raw" in tests
     */
    private inline fun <reified E> rawResourceToInstance(rawFileName: String): E {
        val textAsJson = HomeViewModelTest::class.java.classLoader.getResource(rawFileName).readText()
        return Gson().fromJson(textAsJson, E::class.java)
    }

}