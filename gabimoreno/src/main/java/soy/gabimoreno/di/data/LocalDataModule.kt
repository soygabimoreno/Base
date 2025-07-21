package soy.gabimoreno.di.data

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.di.IO

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Provides
    fun gabiMorenoDatabaseProvider(application: Application): ApplicationDatabase =
        Room
            .databaseBuilder(
                application,
                ApplicationDatabase::class.java,
                "gabimoreno-db",
            ).fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideLocalPremiumAudioDataSource(
        gabiMorenoDatabase: ApplicationDatabase,
        @IO dispatcher: CoroutineDispatcher,
    ): LocalPremiumAudiosDataSource =
        LocalPremiumAudiosDataSource(
            gabiMorenoDatabase,
            dispatcher,
        )
}
