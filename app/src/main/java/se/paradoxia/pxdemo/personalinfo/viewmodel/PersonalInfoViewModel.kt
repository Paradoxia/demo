package se.paradoxia.pxdemo.personalinfo.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.gojuno.koptional.Optional
import io.reactivex.disposables.CompositeDisposable
import se.paradoxia.pxdemo.AppAction
import se.paradoxia.pxdemo.personalinfo.model.PersonalInfoResponse
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.AllOpen
import timber.log.Timber
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
                //cardAboutMe.update(language!!, aboutMeResponse)
            }
        }, { error -> Timber.e(error) }))

    }

    class CardPersonalInfo {

        val persInfoFieldPiTitle = ObservableField<String>()
        val personalFields = mutableListOf<PersonalInfoValue>()

        class PersonalInfoValue {
            val persInfoFieldName = ObservableField<String>()
            val persInfoFieldValue = ObservableField<String>()
        }

        fun update(language: String, personalInfoResponse: PersonalInfoResponse) {

            val personalInfo = if (language == "en") personalInfoResponse.personalInfoEn
            else personalInfoResponse.personalInfoSv

            persInfoFieldPiTitle.set(personalInfo?.personalInfoFields?.piTitle)

            



        }


/*        val persInfoFullName = ObservableField<String>()
        val persInfoDOB = ObservableField<String>()
        val persInfoResidence = ObservableField<String>()
        val persInfoEmail = ObservableField<String>()
        val persInfoPhone = ObservableField<String>()
        val persInfoStatus = ObservableField<String>()

        val persInfoFieldFullName = ObservableField<String>()
        val persInfoFieldDOB = ObservableField<String>()
        val persInfoFieldResidence = ObservableField<String>()
        val persInfoFieldEmail = ObservableField<String>()
        val persInfoFieldPhone = ObservableField<String>()
        val persInfoFieldStatus = ObservableField<String>()*/

    }

    class CardLanguage {

        val languageFieldLangTitle = ObservableField<String>()
        val languages = mutableListOf<LanguageValue>()

        class LanguageValue {
            val languageTitle = ObservableField<String>()
            val languageValue = ObservableField<Int>()
        }

    }


}