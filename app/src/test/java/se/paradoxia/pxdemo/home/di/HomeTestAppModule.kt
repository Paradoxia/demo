package se.paradoxia.pxdemo.home.di

import android.content.Context
import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.provider.RawResourceProvider
import se.paradoxia.pxdemo.provider.SchedulerProvider
import se.paradoxia.pxdemo.provider.SharedPreferencesProvider
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.SchedulerService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-31.
 */
@Module
class HomeTestAppModule(private val customContentService: ContentService) {

    @Provides
    @Singleton
    fun provideApplication(app: HomeTestApp): Context = app

    @Provides
    @Singleton
    fun provideSchedulerService(): SchedulerService {
        return SchedulerProvider()
    }

    @Provides
    @Singleton
    fun provideRawSourceService(app: HomeTestApp): RawResourceService {
        return RawResourceProvider(app)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesService(app: HomeTestApp): SharedPreferencesService {
        return SharedPreferencesProvider(app)
    }

    @Provides
    @Singleton
    fun provideContentService(): ContentService = customContentService

}