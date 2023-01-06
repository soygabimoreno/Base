package soy.gabimoreno.di.data

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.LocalPremiumAudiosDataSource
import soy.gabimoreno.di.IO

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    fun gabiMorenoDatabaseProvider(application: Application): GabiMorenoDatabase =
        Room.databaseBuilder(
            application,
            GabiMorenoDatabase::class.java,
            "gabimoreno-db",
        ).build()

    @Provides
    fun provideLocalPremiumAudioDataSource(
        gabiMorenoDatabase: GabiMorenoDatabase,
        @IO dispatcher: CoroutineDispatcher,
    ): LocalPremiumAudiosDataSource =
        LocalPremiumAudiosDataSource(
            gabiMorenoDatabase,
            dispatcher
        )
}
