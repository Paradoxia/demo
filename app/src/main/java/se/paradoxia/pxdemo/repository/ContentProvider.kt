package se.paradoxia.pxdemo.repository

import android.support.annotation.IdRes
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import io.realm.RealmObject
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.realm.RealmHelper
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.SchedulerService
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-25.
 */

class AboutMeResponseHelper : RealmHelper<AboutMeResponse>
class InfoCardResponseHelper : RealmHelper<InfoCardResponse>

class ContentProvider @Inject constructor(private val restApi: RestApi,
                                          private val schedulerService: SchedulerService,
                                          private val rawResourceService: RawResourceService) : ContentService {

    @Suppress("UNCHECKED_CAST")
    override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
        val contentIdKey = "infocard"
        val infoCardResponseHelper = InfoCardResponseHelper()
        Timber.d("Fetching \"[$contentIdKey]\" content for resources, realm and server ")
        return fetchContentLocallyAndExternally(contentIdKey, infoCardResponseHelper as RealmHelper<RealmObject>,
                schedulerService, rawResourceService, R.raw.infocardresponse,
                InfoCardResponse::class.java as Class<RealmObject>, restApi.getInfoCard() as Observable<RealmObject>)

    }

    @Suppress("UNCHECKED_CAST")
    override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
        val contentIdKey = "aboutme"
        val aboutMeResponseHelper = AboutMeResponseHelper()
        Timber.d("Fetching \"[$contentIdKey]\" content for resources, realm and server ")
        return fetchContentLocallyAndExternally(contentIdKey, aboutMeResponseHelper as RealmHelper<RealmObject>,
                schedulerService, rawResourceService, R.raw.aboutmeresponse,
                AboutMeResponse::class.java as Class<RealmObject>, restApi.getAboutMe() as Observable<RealmObject>)
    }

}

/**
 * Fetching content from local resources -> local realm -> external service
 */
inline fun <reified E> fetchContentLocallyAndExternally(contentIdKey: String, realmHelper: RealmHelper<RealmObject>,
                                                        schedulerService: SchedulerService,
                                                        rawResourceService: RawResourceService,
                                                        @IdRes resId : Int,
                                                        responseClass: Class<RealmObject>,
                                                        restApiMethod: Observable<RealmObject> ): E {

    val fetchFromClient = Observable.create<Any> { emitter ->
        val realmAboutMeResponse: RealmObject? = realmHelper.findById("id", contentIdKey, responseClass)
        if (realmAboutMeResponse != null) {
            Timber.d("Fetched \"[$contentIdKey]\" content from realm")
            emitter.onNext(realmAboutMeResponse.toOptional())
        } else {
            val localAboutMeResponse = rawResourceService.readJson(resId, responseClass)
            Timber.d("Fetched \"[$contentIdKey]\" content from resources")
            emitter.onNext(localAboutMeResponse.toOptional())
        }
        emitter.onComplete()
    }

    val fetchFromServer = restApiMethod
            .subscribeOn(schedulerService.io())
            .flatMap { response: RealmObject ->
                Timber.d("Fetched \"[$contentIdKey]\" content from server")
                realmHelper.saveOrUpdate(response)
                Observable.create<Optional<Any>> { emitter ->
                    emitter.onNext(response.toOptional())
                    emitter.onComplete()
                }
            }
            .onErrorReturn { error ->
                Timber.e(error)
                None
            }

    return Observable.concat<Any>(fetchFromClient, fetchFromServer) as E

}