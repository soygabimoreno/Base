package soy.gabimoreno.remoteconfig.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.remoteconfig.BuildConfig
import soy.gabimoreno.remoteconfig.FirebaseRemoteConfigProvider
import soy.gabimoreno.remoteconfig.RemoteConfigProvider
import soy.gabimoreno.remoteconfig.SecureEncryptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun provideSecureEncryptor(): SecureEncryptor = SecureEncryptor(BuildConfig.MASTER_KEY)

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigProvider(
        firebaseRemoteConfig: FirebaseRemoteConfig,
        secureEncryptor: SecureEncryptor,
    ): RemoteConfigProvider = FirebaseRemoteConfigProvider(firebaseRemoteConfig, secureEncryptor)
}
