package soy.gabimoreno.presentation.screen.courses.list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sell
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import soy.gabimoreno.R
import soy.gabimoreno.core.removeHtmlTags
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Percent
import soy.gabimoreno.presentation.theme.PurpleDark
import soy.gabimoreno.presentation.theme.PurpleLight
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun ItemListCourse(
    audioCourse: AudioCourse,
    modifier: Modifier = Modifier,
    onItemClick: (audioCourseId: String) -> Unit = {},
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopEnd) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors =
                                    listOf(
                                        PurpleDark,
                                        PurpleLight,
                                    ),
                            ),
                        shape = RoundedCornerShape(Spacing.s16),
                    ).border(1.dp, Orange, RoundedCornerShape(Spacing.s16))
                    .clickable { onItemClick(audioCourse.id) },
        ) {
            ItemImage(audioCourse.thumbnailUrl)
            Text(
                text = audioCourse.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(Orange)
                        .padding(Spacing.s8),
            )
            Text(
                text = audioCourse.excerpt.removeHtmlTags(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                lineHeight = 1.5.em,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.s16, vertical = Spacing.s8),
            )
        }
        if (audioCourse.isPurchased) {
            ActiveCourseIcon()
        }
    }
}

@Composable
private fun ActiveCourseIcon() {
    Icon(
        imageVector = Icons.Default.Sell,
        contentDescription = stringResource(R.string.course_active),
        tint = White,
        modifier =
            Modifier
                .padding(Spacing.s4)
                .size(40.dp)
                .rotate(Percent.NINETY),
    )
}

@Composable
private fun ItemImage(audioCourseThumbnailUrl: String) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(audioCourseThumbnailUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
        contentDescription = stringResource(R.string.course_thumbnail),
        contentScale = ContentScale.Crop,
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = Spacing.s96, max = Spacing.s144)
                .clip(
                    RoundedCornerShape(
                        topStart = Spacing.s16,
                        topEnd = Spacing.s16,
                    ),
                ),
        error = painterResource(R.drawable.ic_course_default),
        placeholder = painterResource(R.drawable.ic_course_default),
        alignment = Alignment.BottomStart,
    )
}
