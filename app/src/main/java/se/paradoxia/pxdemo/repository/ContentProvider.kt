package se.paradoxia.pxdemo.repository

import io.reactivex.Observable
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SchedulerService
import javax.inject.Inject

/**
 * Created by mikael on 2018-01-25.
 */
class ContentProvider @Inject constructor(private val restApi: RestApi, private val schedulerService: SchedulerService) : ContentService {

    override fun fetchAboutMe(): Observable<AboutMeResponse> {
        return restApi.getAboutMe()
                .subscribeOn(schedulerService.io())
                .flatMap({ response ->
                    Observable.create<AboutMeResponse> { emitter ->
                        emitter.onNext(response)
                        emitter.onComplete()
                    }
                })


    }

}