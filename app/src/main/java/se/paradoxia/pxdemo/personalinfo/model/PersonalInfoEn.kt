package se.paradoxia.pxdemo.personalinfo.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
class PersonalInfo : RealmObject() {

    @field:SerializedName("languages")
    var languages: RealmList<LanguagesItem?>? = null

    @field:SerializedName("phone")
    var phone: String? = null

    @field:SerializedName("fullName")
    var fullName: String? = null

    @field:SerializedName("dateOfBirth")
    var dateOfBirth: String? = null

    @field:SerializedName("residence")
    var residence: String? = null

    @field:SerializedName("fields")
    var personalInfoFields: PersonalInfoFields? = null

    @field:SerializedName("email")
    var email: String? = null

    @field:SerializedName("status")
    var status: String? = null

}