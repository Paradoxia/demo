package se.paradoxia.pxdemo.di

import android.content.Context
import com.gojuno.koptional.Optional
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import se.paradoxia.pxdemo.home.HomeTestApp
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.model.infocard.InfoCardResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-30.
 */
@Module
class TestAppModule {

    @Provides
    @Singleton
    fun provideApplication(app: HomeTestApp): Context = app

    @Provides
    @Singleton
    fun provideContentService(): ContentService {
        return object:ContentService {
            override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesService(): SharedPreferencesService {
        return object:SharedPreferencesService {
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

}

