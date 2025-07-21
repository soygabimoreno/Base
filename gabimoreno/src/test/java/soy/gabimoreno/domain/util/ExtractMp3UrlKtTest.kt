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

    @Test
    fun `GIVEN a content with another MP3 url before WHEN call THEN get the expected mp3 url`() {
        val rendered =
            "<div class=\"typography\">\n" +
                "<div id=\"ez-toc-container\" class=\"ez-toc-v2_0_59 counter-hierarchy ez-toc-counter ez-toc-custom ez-toc-containe\">\n" +
                "<div class=\"ez-toc-title-container\">\n" +
                "<span class=\"ez-toc-title-toggle\"></span></div>\n" +
                "<nav><ul class='ez-toc-list ez-toc-list-level-1 el-2'><a class=\"ez-toc-link ez-toc-heading-1\"" +
                " href=\"https://gabimoreno.soy/ui-testing-android/#_UI_Testing_en_ANDROID_con_estos_tips\" title=\"Episodio 150 &#8211; " +
                "Domina el UI Testing en ANDROID con estos tips\">Episodio 150 &#os tips</a></li><li class='ez-toc-page-1 ez-toc-heading-" +
                "levhref=\"https://gabimoreno.soy/ui-testing-android/#Podcast_Premium\" title=\"Podcast Premium\">Podcast Premium</a><ul " +
                "class='ez-toc-lg-3\" href=\"https://gabimoreno.soy/ui-testing-android/#Links_del_invitado\"" +
                " title=\"Links del invitado\">Links del invitado</a></lieading-level-3'><a class=\"ez-toc-link ez-toc-heading-4\"" +
                " href=\"https://gabimoreno.soy/ui-testing-android/#Links_de_inks de menciones (UI Tests en Android)\">Links " +
                "de menciones (UI Tests en Android)</a></li><li class='ez-toc-heading-level-3'><a class=\"ez-toc-link ez-toc-heading-5\" " +
                "href=\"https://gabimoreno.soy/ui-testing-android/#Patrones_dseno_de_UI_Tests\" title=\"Patrones de diseño de UI Tests\">" +
                "Patrones de diseño de UI Tests</a></li></ul></li></ul></nav></div>\n" +
                "<h2 class=\"wp-block-heading\"><span class=\"ez-toc-section0_%E2%80%93_Domina_el_UI_Testing_en_ANDROID_con_estos_tips\">" +
                "</span>Episodio 150 &#8211; Domina el UI Testing en ANDROIDn estos tips<span class=\"ez-toc-section-end\"></span></h2>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>En esta charla técnica, exploraremos el fascinante mundo del UI Testing en Android con Sergio Sastre.</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>¿Alguna vez te has preguntado cómo asegurar que las interid funcionen a la perfección? Aquí está tu respuesta.</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>¿Viste ya la <a href=\"https://gabimoreno.soy/senior-arquitectura-testing\">entrevista a XurxoDev</a>?</p>\n" +
                "<iframe title=\"Domina el UI Testing en ANDROID con estos tips\" width=\"500\" height=\"281\" " +
                "src=\"https://www.youtube.com/embed/1TOzPnLsYRc?list=PLwRXwAGGsPEAzeOiKzupkCGSsUBRP-sj5\" frameborder=\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                "allowfullscreen></iframe>\n" +
                "</div></figure>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<figure class=\"wp-block-audio\"><audio controls src=\"$OTHER_MP3_URL\"></audio></figure>\n" +
                "<figure class=\"wp-block-audio\"><audio controls src=\"$MP3_URL\"></audio></figure>\n" +
                "</div>"

        val result = rendered.extractMp3Url()

        result shouldBeEqualTo MP3_URL
    }
}

private const val PREFIX = "https://gabimoreno.soy/wp-content/uploads/"
internal const val OTHER_PREFIX = "https://foo/"

private const val MP3_URL =
    "${PREFIX}GABI-MORENO-Premium-sample.mp3"

private const val OTHER_MP3_URL =
    "${OTHER_PREFIX}foo.mp3"
