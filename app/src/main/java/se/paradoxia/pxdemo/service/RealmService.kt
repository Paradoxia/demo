package se.paradoxia.pxdemo.service

import io.realm.RealmObject
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoResponse

/**
 * Created by mikael on 2018-02-03.
 */
interface RealmService {

    fun fetchAboutMe(): AboutMeResponse?

    fun saveAboutMe(aboutMeResponse: RealmObject)

    fun fetchInfoCard(): InfoCardResponse?

    fun saveInfoCard(infoCardResponse: RealmObject)

    fun fetchPersonalInfo(): PersonalInfoResponse?

    fun savePersonalInfo(personalInfoResponse: RealmObject)

}

