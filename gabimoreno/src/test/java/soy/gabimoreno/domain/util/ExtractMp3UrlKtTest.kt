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

    @Test
    fun `GIVEN content with a link to subscribe before restricted audio block WHEN call THEN get the expected mp3 url`() {
        val rendered =
            "<div class=\"typography\">\n" +
                "<p>Ya está disponible en el canal estable la nueva versión de Android Studio, la 2021.3.1 o más fácil:" +
                " <strong>Dolphin</strong>.</p>\n\n\n\n" +
                "<p>Si te <a href=\"https://gabimoreno.soy/suscribirme\">suscribes</a>, " +
                "podrás escuchar el <strong>audio premium</strong> en el que te cuento esta noticia de viva voz. " +
                "Así podrás estar al día del ecosistema Android mientras haces otras cosas.</p>\n\n\n\n" +
                "<p>Además es muy entretenido. 😉</p>\n\n\n\n" +
                "<figure class=\"wp-block-audio\"><audio controls src=\"$MP3_URL\"></audio></figure>\n\n\n\n\n" +
                "<h2>Novedades en Jetpack Compose</h2>\n\n\n\n<h3>Compose Animation Coordination</h3>\n\n\n\n" +
                "<p>Si tienes las animaciones descritas en un composable preview, ahora desde el Animation Preview" +
                " se puede tanto inspeccionar como coordinar a la vez.</p>\n\n\n\n"

        val result = rendered.extractMp3Url()

        result shouldBeEqualTo MP3_URL
    }
}

private const val MP3_URL =
    "${PREFIX}GABI-MORENO-Premium-sample.mp3"
