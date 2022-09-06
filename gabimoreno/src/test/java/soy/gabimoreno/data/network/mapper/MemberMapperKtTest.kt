package soy.gabimoreno.data.network.mapper

import org.amshove.kluent.shouldBe
import org.junit.Test
import soy.gabimoreno.data.network.model.MemberApiModel

class MemberMapperKtTest {

    @Test
    fun `GIVEN an MemberApiModel list WHEN toDomain THEN return the expected member`() {
        val memberApiModelList = listOf(buildMemberApiModel())

        val result = memberApiModelList.toDomain()

        result.isActive shouldBe IS_ACTIVE
    }

    private fun buildMemberApiModel() = MemberApiModel(
        isActive = IS_ACTIVE
    )
}

private const val IS_ACTIVE = true
