package soy.gabimoreno.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

fun Context.sendEmail(
    to: String = "hola@gabimoreno.soy",
    subject: String = "Sugerencia sobre la app",
    body: String = "Hola, me gustaría sugerir...",
    errorText: String = "No se encontró una app de correo instalada",
    chooserTitle: String = "Enviar email"
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        startActivity(Intent.createChooser(intent, chooserTitle))
    } catch (e: ActivityNotFoundException) {
        Log.w("sendEmail", "No email app found", e)
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
    }
}
