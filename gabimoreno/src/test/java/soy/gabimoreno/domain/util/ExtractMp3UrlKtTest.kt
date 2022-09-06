package soy.gabimoreno.domain.util

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class ExtractMp3UrlKtTest {

    @Test
    fun `GIVEN a rendered content with an mp3 url WHEN call THEN get the expected mp3 url`() {
        val rendered =
            "<div class=\"typography\">\n" +
                "<p>Este es el párrafo inicial del audioCurso.</p>\n\n\n\n</p>\n" +
                "<p>Esto es un contenido restringido</p>\n" +
                "<figure class=\"wp-block-audio\">" +
                "<audio controls src=\"$MP3_URL\"></audio>" +
                "</figure>\n<p>\n\n\n\n\n" +
                "<p>Este es el párrafo final del audiocurso.</p>\n" +
                "</div>"

        val result = rendered.extractMp3Url()

        result shouldBeEqualTo MP3_URL
    }

    @Test
    fun `GIVEN a rendered content without an mp3 url WHEN call THEN get the expected mp3 url`() {
        val rendered =
            "<div class=\"typography\">\n" +
                "<p>Este es el párrafo inicial del audioCurso.</p>\n\n\n\n</p>\n" +
                "<p>Esto es un contenido restringido</p>\n" +
                "<figure class=\"wp-block-audio\">" +
                "</figure>\n<p>\n\n\n\n\n" +
                "<p>Este es el párrafo final del audiocurso.</p>\n" +
                "</div>"

        val result = rendered.extractMp3Url()

        result shouldBeEqualTo null
    }
}

private const val MP3_URL =
    "https://gabimoreno.soy/wp-content/uploads/GABI-MORENO-Premium-sample.mp3"
