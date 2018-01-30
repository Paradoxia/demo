package se.paradoxia.pxdemo.di

/**
 * Created by mikael on 2018-01-30.
 */
import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.home.HomeFragment


@Module
abstract class TestActivityBuilderModule {

    @ContributesAndroidInjector(modules = [TestMainActivityModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [TestViewModelModule::class])
    internal abstract fun bindHomeFragment(): HomeFragment


}