package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.repository.ContentProvider
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.SchedulerService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideContentService(schedulerService: SchedulerService): ContentService {
        return ContentProvider(RestApi.create(), schedulerService)
    }

}