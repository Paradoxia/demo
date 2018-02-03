package se.paradoxia.pxdemo.home.di

import android.app.Application
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.provider.RawResourceProvider
import se.paradoxia.pxdemo.provider.SchedulerProvider
import se.paradoxia.pxdemo.provider.SharedPreferencesProvider
import se.paradoxia.pxdemo.service.*
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-31.
 */
@Module
class HomeTestAppModule(private val customContentService: ContentService, private val customPermissionService: PermissionService) {

    @Provides
    @Singleton
    fun provideApplication(app: HomeTestApp): Application = app

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

    @Provides
    @Singleton
    fun homeViewModel(contentService: ContentService, sharedPreferencesService: SharedPreferencesService): HomeViewModel {
        return Mockito.spy(HomeViewModel(contentService, sharedPreferencesService))
    }

    @Provides
    @Singleton
    fun providePermissionService(): PermissionService {
        return customPermissionService
    }


}