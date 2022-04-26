package soy.gabimoreno.bike.di

import com.clj.fastble.BleManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object MainModule {

    @Provides
    fun provideBleManager(): BleManager = BleManager.getInstance()
}
