package soy.gabimoreno.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PremiumAudioDbModel::class
    ],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class GabiMorenoDatabase : RoomDatabase() {
    abstract fun premiumAudioDbModelDao(): PremiumAudioDbModelDao
}
