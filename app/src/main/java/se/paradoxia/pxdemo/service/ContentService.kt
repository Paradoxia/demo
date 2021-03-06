package se.paradoxia.pxdemo.service

import com.gojuno.koptional.Optional
import io.reactivex.Observable
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoResponse

/**
 * Created by mikael on 2018-01-25.
 */
interface ContentService {

    fun fetchAboutMe(): Observable<Optional<AboutMeResponse>>

    fun fetchInfoCard(): Observable<Optional<InfoCardResponse>>

    fun fetchPersonalInfo(): Observable<Optional<PersonalInfoResponse>>

}