package se.paradoxia.pxdemo.realm

import io.realm.RealmObject
import se.paradoxia.pxdemo.home.model.aboutme.InfoCardResponse
import se.paradoxia.pxdemo.home.model.infocard.AboutMeResponse
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoResponse
import se.paradoxia.pxdemo.service.RealmService
import javax.inject.Inject

const val ABOUT_ME_ID_FIELD = "id"
const val ABOUT_ME_ID = "aboutme"

const val INFO_CARD_ID_FIELD = "id"
const val INFO_CARD_ID = "infocard"

const val PERSONAL_INFO_ID_FIELD = "id"
const val PERSONAL_INFO_ID = "personalinfo"

class RealmProvider @Inject constructor(private val realmInstanceMaker: RealmInstanceMaker) :
    RealmService {

    class AboutMeResponseHelper(realmInstanceMaker: RealmInstanceMaker) :
        RealmHelper<AboutMeResponse>(realmInstanceMaker)

    class InfoCardResponseHelper(realmInstanceMaker: RealmInstanceMaker) :
        RealmHelper<InfoCardResponse>(realmInstanceMaker)

    class PersonalInfoResponseHelper(realmInstanceMaker: RealmInstanceMaker) :
        RealmHelper<PersonalInfoResponse>(realmInstanceMaker)

    override fun fetchAboutMe(): AboutMeResponse? {
        val responseHelper: RealmHelper<AboutMeResponse> = AboutMeResponseHelper(realmInstanceMaker)
        return responseHelper.findById(
            ABOUT_ME_ID_FIELD,
            ABOUT_ME_ID,
            AboutMeResponse::class.java
        )
    }

    override fun saveAboutMe(aboutMeResponse: RealmObject) {
        val responseHelper: RealmHelper<AboutMeResponse> = AboutMeResponseHelper(realmInstanceMaker)
        responseHelper.saveOrUpdate(aboutMeResponse as AboutMeResponse)
    }

    override fun fetchInfoCard(): InfoCardResponse? {
        val responseHelper: RealmHelper<InfoCardResponse> =
            InfoCardResponseHelper(realmInstanceMaker)
        return responseHelper.findById(
            INFO_CARD_ID_FIELD,
            INFO_CARD_ID,
            InfoCardResponse::class.java
        )
    }

    override fun saveInfoCard(infoCardResponse: RealmObject) {
        val responseHelper: RealmHelper<InfoCardResponse> =
            InfoCardResponseHelper(realmInstanceMaker)
        responseHelper.saveOrUpdate(infoCardResponse as InfoCardResponse)
    }


    override fun fetchPersonalInfo(): PersonalInfoResponse? {
        val responseHelper: RealmHelper<PersonalInfoResponse> =
            PersonalInfoResponseHelper(realmInstanceMaker)
        return responseHelper.findById(
            PERSONAL_INFO_ID_FIELD,
            PERSONAL_INFO_ID,
            PersonalInfoResponse::class.java
        )

    }

    override fun savePersonalInfo(personalInfoResponse: RealmObject) {
        val responseHelper: RealmHelper<PersonalInfoResponse> =
            PersonalInfoResponseHelper(realmInstanceMaker)
        responseHelper.saveOrUpdate(personalInfoResponse as PersonalInfoResponse)
    }

}