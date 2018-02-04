package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.Provides
import se.paradoxia.pxdemo.api.RestApi
import se.paradoxia.pxdemo.realm.RealmInstanceMaker
import se.paradoxia.pxdemo.realm.RealmProvider
import se.paradoxia.pxdemo.repository.ContentProvider
import se.paradoxia.pxdemo.service.ContentService
import se.paradoxia.pxdemo.service.RawResourceService
import se.paradoxia.pxdemo.service.RealmService
import se.paradoxia.pxdemo.service.SchedulerService
import javax.inject.Singleton

/**
 * Created by mikael on 2018-01-20.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideContentService(schedulerService: SchedulerService, rawResourceService: RawResourceService, realmService: RealmService): ContentService {
        return ContentProvider(RestApi.create(), schedulerService, rawResourceService, realmService)
    }

    @Provides
    @Singleton
    fun provideRealmInstanceMaker(): RealmInstanceMaker {
        return object : RealmInstanceMaker() {}
    }

    @Provides
    @Singleton
    fun provideRealmService(realmInstanceMaker: RealmInstanceMaker): RealmService {
        return RealmProvider(realmInstanceMaker)
    }

}