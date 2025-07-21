package soy.gabimoreno.framework

import android.util.Log

class KLog {
    companion object {
        var debug = true
            private set

        /**
         * This method must be called at the very beginning within the onCreate() of the Application class
         * (or first activity to lunch) for ensuring logs are not shown on release version.
         */
        fun launch(debug: Boolean) {
            Companion.debug = debug
        }

        fun v(s: String) {
            if (debug) {
                Throwable().stackTrace[1].apply {
                    Log.v(
                        generateTag(),
                        generateMessage(s),
                    )
                }
            }
        }

        fun d(s: String) {
            if (debug) {
                Throwable().stackTrace[1].apply {
                    Log.d(
                        generateTag(),
                        generateMessage(s),
                    )
                }
            }
        }

        fun i(s: String) {
            if (debug) {
                Throwable().stackTrace[1].apply {
                    Log.i(
                        generateTag(),
                        generateMessage(s),
                    )
                }
            }
        }

        fun w(s: String) {
            if (debug) {
                Throwable().stackTrace[1].apply {
                    Log.w(
                        generateTag(),
                        generateMessage(s),
                    )
                }
            }
        }

        fun e(s: String) {
            if (debug) {
                Throwable().stackTrace[1].apply {
                    Log.e(
                        generateTag(),
                        generateMessage(s),
                    )
                }
            }
        }
    }
}

private fun StackTraceElement.generateTag() = className.substringAfterLast(".")
private fun StackTraceElement.generateMessage(msg: Any) = "$methodName() $msg"
