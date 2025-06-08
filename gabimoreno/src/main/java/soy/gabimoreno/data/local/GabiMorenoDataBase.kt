package soy.gabimoreno.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseItemDbModelDao
import soy.gabimoreno.data.local.audiocourse.dao.AudioCourseTransactionDao
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.playlist.dao.PlaylistDbModelDao
import soy.gabimoreno.data.local.playlist.dao.PlaylistItemDbModelDao
import soy.gabimoreno.data.local.playlist.dao.PlaylistTransactionDao
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModelDao

@Database(
    entities = [
        PremiumAudioDbModel::class,
        AudioCourseDbModel::class,
        AudioCourseItemDbModel::class,
        PlaylistDbModel::class,
        PlaylistItemsDbModel::class,
    ],
    version = 5,
)
@TypeConverters(Converters::class)
abstract class GabiMorenoDatabase : RoomDatabase() {
    abstract fun premiumAudioDbModelDao(): PremiumAudioDbModelDao
    abstract fun audioCourseDbModelDao(): AudioCourseDbModelDao
    abstract fun audioCourseItemDbModelDao(): AudioCourseItemDbModelDao
    abstract fun audioCourseTransactionDao(): AudioCourseTransactionDao
    abstract fun playlistDbModelDao(): PlaylistDbModelDao
    abstract fun playlistItemDbModelDao(): PlaylistItemDbModelDao
    abstract fun playlistTransactionDao(): PlaylistTransactionDao
}
