package se.paradoxia.pxdemo.di

import android.content.Context
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

 /*   @Provides
    @Singleton
    fun provideApplication(app: App): Context = app*/

    @Provides
    @AppContext
//    @Singleton
    fun provideAppContext(app: App): Context = app

    @Provides
    @Singleton
    fun provideSchedulerService(): SchedulerService {
        return SchedulerProvider()
    }

}