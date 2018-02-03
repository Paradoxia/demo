package se.paradoxia.pxdemo.repository

import android.support.annotation.IdRes
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Observable
import io.realm.RealmObject
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.RealmService
import se.paradoxia.pxdemo.service.SchedulerService
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-25.
 */

class ContentProvider @Inject constructor(private val restApi: RestApi,
                                          private val schedulerService: SchedulerService,
                                          private val rawResourceService: RawResourceService,
                                          private val realmService: RealmService) : ContentService {

    @Suppress("UNCHECKED_CAST")
    override fun fetchInfoCard(): Observable<Optional<InfoCardResponse>> {
        Timber.d("Fetching InfoCard content for resources, realm and server ")
        return fetchContentLocallyAndExternally(realmService::fetchInfoCard, realmService::saveInfoCard,
                schedulerService, rawResourceService, R.raw.infocardresponse,
                InfoCardResponse::class.java as Class<RealmObject>, restApi.getInfoCard() as Observable<RealmObject>)

    }

    @Suppress("UNCHECKED_CAST")
    override fun fetchAboutMe(): Observable<Optional<AboutMeResponse>> {
        Timber.d("Fetching AboutMe content for resources, realm and server ")
        return fetchContentLocallyAndExternally(realmService::fetchAboutMe, realmService::saveAboutMe,
                schedulerService, rawResourceService, R.raw.aboutmeresponse,
                AboutMeResponse::class.java as Class<RealmObject>, restApi.getAboutMe() as Observable<RealmObject>)
    }

}

/**
 * Fetching content FROM [local realm] OR [local resources] THEN external server
 */
inline fun <reified E> fetchContentLocallyAndExternally(crossinline realmFetcher: () -> RealmObject?,
                                                        crossinline realmSaver: (obj: RealmObject) -> Unit,
                                                        schedulerService: SchedulerService,
                                                        rawResourceService: RawResourceService,
                                                        @IdRes resId: Int,
                                                        responseClass: Class<RealmObject>,
                                                        restApiMethod: Observable<RealmObject>): E {

    val fetchFromClient = Observable.create<Any> { emitter ->
        val realmObject: RealmObject? = realmFetcher()
        if (realmObject != null) {
            Timber.d("Fetched \"[${realmObject.javaClass.name}]\" content from realm")
            emitter.onNext(realmObject.toOptional())
        } else {
            val localObject = rawResourceService.readJson(resId, responseClass)
            Timber.d("Fetched \"[${localObject.javaClass.name}]\" content from resources")
            emitter.onNext(localObject.toOptional())
        }
        emitter.onComplete()
    }

    val fetchFromServer = restApiMethod
            .subscribeOn(schedulerService.io())
            .flatMap { response: RealmObject ->
                Timber.d("Fetched \"[${response.javaClass.name}]\" content from server")
                realmSaver(response)
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