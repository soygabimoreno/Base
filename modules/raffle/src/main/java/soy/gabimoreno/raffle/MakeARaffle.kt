package soy.gabimoreno.raffle

fun makeARaffle(
    prizes: Set<Prize>,
    participants: Set<Participant>,
): List<Winner> {
    val winners = mutableListOf<Winner>()
    val remainingParticipants = participants.toMutableList()
    prizes.forEach { prize ->
        val position = (0 until remainingParticipants.size).random()
        val luckyParticipant = remainingParticipants[position]
        winners.add(Winner(prize, luckyParticipant))
        remainingParticipants.removeAt(position)
        if (remainingParticipants.isEmpty()) return winners
    }
    return winners
}

fun main() {
    val prizes =
        setOf(
            Prize(id = 1, name = "Prize 1"),
            Prize(id = 2, name = "Prize 2"),
            Prize(id = 3, name = "Prize 3"),
            Prize(id = 4, name = "Prize 4"),
        )
    val participants =
        setOf(
            Participant(id = 1, name = "Participant 1"),
            Participant(id = 2, name = "Participant 2"),
            Participant(id = 3, name = "Participant 3"),
            Participant(id = 4, name = "Participant 4"),
            Participant(id = 5, name = "Participant 5"),
        )
    val result = makeARaffle(prizes, participants)
    result.forEach { winner ->
        println("${winner.participant.name} has won a ${winner.prize.name}")
    }
}
