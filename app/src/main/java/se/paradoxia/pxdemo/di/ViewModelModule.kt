package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.home.HomeViewModel
import se.paradoxia.pxdemo.service.ContentService

@Module
class ViewModelModule {

    @Provides
    fun homeViewModel(contentService: ContentService): HomeViewModel {
        return HomeViewModel(contentService)
    }

}