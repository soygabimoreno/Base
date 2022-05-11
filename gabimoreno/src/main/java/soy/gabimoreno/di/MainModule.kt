package soy.gabimoreno.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.player.Player

@InstallIn(SingletonComponent::class)
@Module
object MainModule {

    @Provides
    fun providePlayer(@ApplicationContext appContext: Context): Player = Player(appContext)
}
