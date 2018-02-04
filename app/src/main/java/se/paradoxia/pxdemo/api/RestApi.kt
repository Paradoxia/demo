package se.paradoxia.pxdemo.api

import io.reactivex.Observable
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse

/**
 * Created by mikael on 2018-01-20.
 */
interface RestApi {

    @retrofit2.http.GET("aboutme")
    fun getAboutMe(): Observable<AboutMeResponse>

    @retrofit2.http.GET("infocard")
    fun getInfoCard(): Observable<InfoCardResponse>


    companion object Factory {
        fun create(): RestApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl(BuildConfig.CONTENT_API_BASE_URL)
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }

}