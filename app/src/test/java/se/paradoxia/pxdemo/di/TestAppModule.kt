package se.paradoxia.pxdemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-30.
 */
@Module
class TestAppModule {

    @Provides
    @Singleton
    fun provideApplication(app: TestApp): Context = app

}

