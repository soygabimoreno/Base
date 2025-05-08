package soy.gabimoreno.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import soy.gabimoreno.data.local.GabiMorenoDatabase
import soy.gabimoreno.data.local.premiumaudio.LocalPremiumAudiosDataSource
import soy.gabimoreno.di.data.LocalDataModule
import javax.inject.Named

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDataModule::class]
)
object TestLocalDataModule {

    @Provides
    @Named(DB_TEST_NAME)
    fun gabiMorenoDatabaseProvider(application: Application): GabiMorenoDatabase =
        Room.inMemoryDatabaseBuilder(
            application, GabiMorenoDatabase::class.java
        ).allowMainThreadQueries()
            .build()

    @Provides
    fun provideLocalPremiumAudioDataSource(
        @Named(DB_TEST_NAME)
        gabiMorenoDatabase: GabiMorenoDatabase,
        @IO dispatcher: CoroutineDispatcher,
    ): LocalPremiumAudiosDataSource =
        LocalPremiumAudiosDataSource(
            gabiMorenoDatabase,
            dispatcher
        )
}

private const val DB_TEST_NAME = "gabimoreno-db-test"
