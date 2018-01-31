package se.paradoxia.pxdemo.home

import dagger.Module
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SharedPreferencesService

/**
 * Created by mikael on 2018-01-31.
 */
@Module
interface HomeTestViewModelModuleInterface {

    fun homeViewModel(contentService: ContentService, sharedPreferencesService: SharedPreferencesService): HomeViewModel

    /*@Binds
    fun provideContentService(schedulerService: SchedulerService, rawResourceService: RawResourceService): ContentService*/

}