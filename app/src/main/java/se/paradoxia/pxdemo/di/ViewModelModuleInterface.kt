package se.paradoxia.pxdemo.di

import dagger.Module
import se.paradoxia.pxdemo.home.HomeViewModel
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService

/**
 * Created by mikael on 2018-01-31.
 */
@Module
interface ViewModelModuleInterface {

    fun homeViewModel(contentService: ContentService, sharedPreferencesService: SharedPreferencesService): HomeViewModel

}