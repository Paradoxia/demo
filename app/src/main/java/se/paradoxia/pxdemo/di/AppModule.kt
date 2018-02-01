package se.paradoxia.pxdemo.di

import android.app.Application
import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.provider.SchedulerProvider
import se.paradoxia.pxdemo.service.SchedulerService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Module(includes = [RepositoryModule::class, UtilModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(app: App): Application = app

    @Provides
    @Singleton
    fun provideSchedulerService(): SchedulerService {
        return SchedulerProvider()
    }

}