package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.home.HomeViewModel
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService

@Module
class ViewModelModule {

    @Provides
    fun homeViewModel(contentService: ContentService, sharedPreferencesService: SharedPreferencesService): HomeViewModel {
        return HomeViewModel(contentService, sharedPreferencesService)
    }

}