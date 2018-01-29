package se.paradoxia.pxdemo.repository

import android.annotation.SuppressLint
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.realm.RealmHelper
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.SchedulerService
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-25.
 */
class ContentProvider @Inject constructor(private val restApi: RestApi,
                                          private val schedulerService: SchedulerService,
                                          private val rawResourceService: RawResourceService) : ContentService, RealmHelper<AboutMeResponse>() {

    @SuppressLint("ResourceType")
    override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {

        Timber.d("Fetching \"About Me\" content for resources, realm and server ")

        val fetchFromClient = Observable.create<Optional<AboutMeResponse>> { emitter ->
            val realmAboutMeResponse = this.findById("id", "aboutme", AboutMeResponse::class.java)
            if (realmAboutMeResponse != null) {
                Timber.d("Fetched \"About Me\" content from realm")
                emitter.onNext(realmAboutMeResponse.toOptional())
            } else {
                val localAboutMeResponse: AboutMeResponse = rawResourceService.readJson(R.raw.aboutmeresponse, AboutMeResponse::class.java)
                Timber.d("Fetched \"About Me\" content from resources")
                emitter.onNext(localAboutMeResponse.toOptional())
            }
            emitter.onComplete()
        }

        val fetchFromServer = restApi.getAboutMe()
                .subscribeOn(schedulerService.io())
                .flatMap { response: AboutMeResponse ->
                    Timber.d("Fetched \"About Me\" content from server")
                    this.saveOrUpdate(response)
                    Observable.create<Optional<AboutMeResponse>> { emitter ->
                        emitter.onNext(response.toOptional())
                        emitter.onComplete()
                    }
                }
                .onErrorReturn { error ->
                    Timber.e(error)
                    None
                }

        return Observable.concat<Optional<AboutMeResponse>>(fetchFromClient, fetchFromServer)

    }

}