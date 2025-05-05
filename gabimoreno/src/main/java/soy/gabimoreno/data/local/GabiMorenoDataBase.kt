package soy.gabimoreno.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseDbModelDao
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseItemDbModelDao
import soy.gabimoreno.data.local.audiocourses.dao.AudioCourseTransactionDao
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseItemDbModel

@Database(
    entities = [
        PremiumAudioDbModel::class,
        AudioCourseDbModel::class,
        AudioCourseItemDbModel::class,
    ],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class GabiMorenoDatabase : RoomDatabase() {
    abstract fun premiumAudioDbModelDao(): PremiumAudioDbModelDao
    abstract fun audioCourseDbModelDao(): AudioCourseDbModelDao
    abstract fun audioCourseItemDbModelDao(): AudioCourseItemDbModelDao
    abstract fun audioCourseTransactionDao(): AudioCourseTransactionDao
}
