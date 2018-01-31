package se.paradoxia.pxdemo.home

import android.content.Context
import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.di.RepositoryModule
import se.paradoxia.pxdemo.di.UtilModule
import se.paradoxia.pxdemo.provider.SchedulerProvider
import se.paradoxia.pxdemo.service.SchedulerService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-31.
 */
@Module(includes = [RepositoryModule::class, UtilModule::class])
class HomeTestAppModule {

    @Provides
    @Singleton
    fun provideApplication(app: HomeTestApp): Context = app

    @Provides
    @Singleton
    fun provideSchedulerService(): SchedulerService {
        return SchedulerProvider()
    }

}