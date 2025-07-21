package soy.gabimoreno.data.local.audiocourse.model

import androidx.room.Embedded
import androidx.room.Relation

data class AudioCourseWithItems(
    @Embedded val course: AudioCourseDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "idAudioCourse",
    )
    val audios: List<AudioCourseItemDbModel>,
)
