package soy.gabimoreno.presentation.screen.player

enum class PlaybackSpeed(
    val label: String,
    val speed: Float,
) {
    SPEED_1X("1x", SPEED_1),
    SPEED_1_25X("1.25x", SPEED_1_25),
    SPEED_1_5X("1.5x", SPEED_1_5),
    SPEED_1_75X("1.75x", SPEED_1_75),
    SPEED_2X("2x", SPEED_2),
    ;

    override fun toString(): String = label
}

private const val SPEED_1 = 1.0f
private const val SPEED_1_25 = 1.25f
private const val SPEED_1_5 = 1.5f
private const val SPEED_1_75 = 1.75f
private const val SPEED_2 = 2.0f
