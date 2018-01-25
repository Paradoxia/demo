package se.paradoxia.pxdemo.api

import io.reactivex.Observable
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse

/**
 * Created by mikael on 2018-01-20.
 */
interface RestApi {

    @retrofit2.http.GET("aboutme")
    fun getAboutMe(): Observable<AboutMeResponse>

    companion object Factory {
        fun create(): RestApi {
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .baseUrl("http://www.paradoxia.se/api/content/")
                    .build()

            return retrofit.create(RestApi::class.java)
        }
    }

}