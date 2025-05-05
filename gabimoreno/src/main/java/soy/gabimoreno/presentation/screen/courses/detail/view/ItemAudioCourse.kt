package soy.gabimoreno.presentation.screen.courses.detail.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import soy.gabimoreno.R
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.presentation.theme.Black
import soy.gabimoreno.presentation.theme.GabiMorenoTheme
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun ItemAudioCourse(
    audioCourseItem: AudioCourseItem,
    audioCourseTitle: String,
    onItemClicked: (audioCourseItem: AudioCourseItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(Spacing.s8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .clickable { onItemClicked(audioCourseItem) }
                .weight(0.85f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(audioCourseItem.title, color = Black, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(Spacing.s4))
            Text(
                audioCourseTitle,
                color = Black.copy(alpha = 0.8f),
                fontWeight = FontWeight.Light
            )
        }
        Icon(
            modifier = Modifier
                .size(Spacing.s32)
                .weight(0.15f),
            imageVector = Icons.Default.Headset,
            contentDescription = stringResource(R.string.course_listened),
            tint = if (audioCourseItem.hasBeenListened) Black.copy(alpha = 0.8f) else Black.copy(
                alpha = 0.2f
            ),
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ItemAudioCoursePreview() {
    GabiMorenoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .border(1.dp, Gray),
            verticalArrangement = Arrangement.Center
        ) {
            ItemAudioCourse(
                audioCourseTitle = "Audio curso",
                audioCourseItem = AudioCourseItem(
                    id = "1",
                    title = "item title",
                    url = "item url",
                    hasBeenListened = true
                ),
                onItemClicked = {}
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Black.copy(alpha = 0.2f))
            )
            ItemAudioCourse(
                audioCourseTitle = "Audio curso",
                audioCourseItem = AudioCourseItem(
                    id = "1",
                    title = "item title",
                    url = "item url",
                    hasBeenListened = false
                ),
                onItemClicked = {}
            )
        }
    }
}
