package soy.gabimoreno.data.local.audiocourse.model

import androidx.room.Embedded
import androidx.room.Relation

data class AudioCourseWithItems(
    @Embedded val audioCourseDbModel: AudioCourseDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "idAudioCourse",
    )
    val audioCourseItemDbModels: List<AudioCourseItemDbModel>,
)
