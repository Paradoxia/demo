package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.personalinfo.viewmodel.PersonalInfoViewModel
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import se.paradoxia.pxdemo.util.AllOpen

@AllOpen
@Module
class ViewModelModule {

    @Provides
    fun provideHomeViewModel(
        contentService: ContentService,
        sharedPreferencesService: SharedPreferencesService
    ): HomeViewModel {
        return HomeViewModel(contentService, sharedPreferencesService)
    }

    @Provides
    fun providePersonalInfoModel(
        contentService: ContentService,
        sharedPreferencesService: SharedPreferencesService
    ): PersonalInfoViewModel {
        return PersonalInfoViewModel(contentService, sharedPreferencesService)
    }

}