package soy.gabimoreno.presentation.ui.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.theme.White

@Composable
fun FlatIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onItemClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(Spacing.s32),
        )
    }
}
