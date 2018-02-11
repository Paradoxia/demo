package se.paradoxia.pxdemo.personalinfo.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.gojuno.koptional.Optional
import io.reactivex.disposables.CompositeDisposable
import se.paradoxia.pxdemo.AppAction
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfo
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoFields
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoResponse
import se.paradoxia.pxdemo.personalinfo.view.PersonalInfoHolder
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.ViewTypeMapper
import timber.log.Timber
import java.lang.reflect.Modifier
import javax.inject.Inject

/**
 * Created by mikael on 2018-02-05.
 */
@AllOpen
class PersonalInfoViewModel @Inject constructor(
    private val contentService: ContentService,
    private val sharedPreferencesService: SharedPreferencesService
) : ViewModel(), AppAction {

    lateinit var cardPersonalInfo: CardPersonalInfo

    var language: String? = null

    val disposables = CompositeDisposable()

    override fun selectLangEN() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectLangSV() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun init() {
        cardPersonalInfo = CardPersonalInfo()
        loadContent()
    }

    internal fun loadContent() {
        language = sharedPreferencesService.getString(
            "language",
            groupKey = null,
            defaultValue = "en"
        )!!

        disposables.add(contentService.fetchPersonalInfo().subscribe({ response: Optional<PersonalInfoResponse> ->
            val personalInfoResponse: PersonalInfoResponse? = response.toNullable()
            if (personalInfoResponse != null) {
                cardPersonalInfo.update(language!!, personalInfoResponse)
            }
        }, { error -> Timber.e(error) }))

    }

    class CardPersonalInfo {

        val title = ObservableField<String>()
        val fieldsAndValues = ObservableArrayList<PersonalInfoValue>()

        class PersonalInfoValue(
            val fieldName: String?,
            val fieldValue: String?
        )

        fun update(language: String, personalInfoResponse: PersonalInfoResponse) {

            val values = if (language == "en") personalInfoResponse.personalInfoEn
            else personalInfoResponse.personalInfoSv
            val fieldValues = extractFieldValues(values as Any, PersonalInfo::class.java)

            val fields =
                if (language == "en") personalInfoResponse.personalInfoEn?.personalInfoFields
                else personalInfoResponse.personalInfoSv?.personalInfoFields
            val fieldNames = extractFieldValues(
                fields as Any,
                PersonalInfoFields::class.java
            )

            fieldNames.forEach {
                fieldsAndValues.add(
                    PersonalInfoValue(
                        it.value as String?, fieldValues[it.key] as String?
                    )
                )

            }

            title.set(values.personalInfoFields?.piTitle)

        }

        private fun extractFieldValues(data: Any, clazz: Class<*>): Map<String, Any?> {
            val map = mutableMapOf<String, Any?>()
            val fields = clazz.declaredFields.map { it.isAccessible = true; it }
            fields.forEach { field ->
                if (!Modifier.isStatic(field.modifiers)) {
                    map[field.name] = field.get(data)
                }
            }
            return map
        }

    }

    fun getCards() = listOf(cardPersonalInfo)

    fun getViewTypeMap() = listOf(
        ViewTypeMapper(
            PersonalInfoViewModel.CardPersonalInfo::class.java,
            R.layout.card_personal_info,
            PersonalInfoHolder::class.java
        )
    )

    class CardLanguage {

        val languageFieldLangTitle = ObservableField<String>()
        val languages = mutableListOf<LanguageValue>()

        class LanguageValue {
            val languageTitle = ObservableField<String>()
            val languageValue = ObservableField<Int>()
        }

    }


}