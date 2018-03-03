package se.paradoxia.pxdemo.personalinfo.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.ModelField

@AllOpen
class PersonalInfo : RealmObject() {

    @field:SerializedName("fullName")
    @ModelField(order = 0, isField = true)
    var fullName: String? = null

    @field:SerializedName("dateOfBirth")
    @ModelField(order = 1, isField = true)
    var dateOfBirth: String? = null

    @field:SerializedName("residence")
    @ModelField(order = 2, isField = true)
    var residence: String? = null

    @field:SerializedName("email")
    @ModelField(order = 3, isField = true)
    var email: String? = null

    @field:SerializedName("phone")
    @ModelField(order = 4, isField = true)
    var phone: String? = null

    @field:SerializedName("status")
    @ModelField(order = 5, isField = true)
    var status: String? = null

    @field:SerializedName("languages")
    @ModelField(order = 0, isField = false)
    var languages: RealmList<LanguagesItem?>? = null

    @field:SerializedName("fields")
    @ModelField(order = 0, isField = false)
    var personalInfoFields: PersonalInfoFields? = null

    companion object {
        fun displayedFields() : List<String> {
            return PersonalInfo::class.java.declaredFields.filter {
                it.getAnnotation(ModelField::class.java)?.isField != null &&
                        it.getAnnotation(ModelField::class.java).isField
            }.sortedBy { it.getAnnotation(ModelField::class.java).order }
                .map { it.name }
        }
    }

}