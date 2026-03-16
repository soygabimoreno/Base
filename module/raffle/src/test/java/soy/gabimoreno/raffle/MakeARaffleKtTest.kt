package soy.gabimoreno.raffle

import org.amshove.kluent.fail
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class MakeARaffleKtTest {
    @Test
    fun `GIVEN more participants than prizes WHEN makeARaffle THEN get the expected winners`() {
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

        checkResult(result, prizes, participants)
    }

    @Test
    fun `GIVEN less participants than prizes WHEN makeARaffle THEN get the expected winners`() {
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
            )

        val result = makeARaffle(prizes, participants)

        checkResult(result, prizes, participants)
    }

    private fun checkResult(
        result: List<Winner>,
        prizes: Set<Prize>,
        participants: Set<Participant>,
    ) {
        result.size shouldBeEqualTo prizes.size.coerceAtMost(participants.size)

        val givenPrizes = mutableListOf<Prize>()
        val luckyParticipants = mutableListOf<Participant>()
        result.forEach { winner ->
            val prize = winner.prize
            if (givenPrizes.contains(prize)) {
                fail("$prize was given before")
            }
            givenPrizes.add(prize)

            val luckyParticipant = winner.participant
            if (luckyParticipants.contains(luckyParticipant)) {
                fail("$luckyParticipant was selected before")
            }
            luckyParticipants.add(luckyParticipant)
        }
    }
}
