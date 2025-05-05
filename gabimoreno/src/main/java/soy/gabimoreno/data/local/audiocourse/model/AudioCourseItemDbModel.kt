package soy.gabimoreno.data.local.audiocourse.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "AudioCourseItems",
    foreignKeys = [
        ForeignKey(
            entity = AudioCourseDbModel::class,
            parentColumns = ["id"],
            childColumns = ["idAudioCourse"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idAudioCourse")]
)
data class AudioCourseItemDbModel(
    @PrimaryKey
    val id: String,
    val idAudioCourse: String,
    val title: String,
    val url: String,
    val hasBeenListened: Boolean,
)
