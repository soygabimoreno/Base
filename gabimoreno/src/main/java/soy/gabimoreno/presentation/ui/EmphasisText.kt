package soy.gabimoreno.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmphasisText(
    text: String,
    contentAlpha: Float = ContentAlpha.medium,
    style: TextStyle = MaterialTheme.typography.body1,
) {
    CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
        Text(
            text,
            style = style
        )
    }
}

@Composable
fun SelectableEmphasisText(
    text: String,
    modifier: Modifier = Modifier,
    contentAlpha: Float = ContentAlpha.medium,
    style: TextStyle = MaterialTheme.typography.body1,
) {
    SelectionContainer(modifier = modifier) {
        EmphasisText(
            text = text,
            contentAlpha = contentAlpha,
            style = style
        )
    }
}

@Preview(name = "SelectableEmphasisText with long HTML content")
@Composable
private fun SelectableEmphasisTextWithLongContentPreview() {
    SelectableEmphasisText(
        text = """
        <p>Descubre el UNIT TESTING de la mano de Sergio Sastre. Aprende y potencia esta skill para impulsar tu carrera de Android Developer. 🎯</p>
        <p>
        👉🏼 NOTAS DEL EPISODIO:</p>
        <p>https://gabimoreno.soy/unit-testing-topic3-2024</p>
        """.trimIndent(),
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(8.dp)
    )
}
