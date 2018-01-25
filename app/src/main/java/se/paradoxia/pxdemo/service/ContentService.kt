package se.paradoxia.pxdemo.service

import io.reactivex.Observable
import se.paradoxia.pxdemo.model.aboutme.AboutMeResponse

/**
 * Created by mikael on 2018-01-25.
 */
interface ContentService {

    fun fetchAboutMe() : Observable<AboutMeResponse>

}