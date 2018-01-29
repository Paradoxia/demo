package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.provider.RawResourceProvider
import se.paradoxia.pxdemo.provider.SharedPreferencesProvider
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.SharedPreferencesService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Module
class UtilModule {

    @Provides
    @Singleton
    fun provideRawSourceService(app: App): RawResourceService {
        return RawResourceProvider(app)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesService(app: App): SharedPreferencesService {
        return SharedPreferencesProvider(app)
    }

}