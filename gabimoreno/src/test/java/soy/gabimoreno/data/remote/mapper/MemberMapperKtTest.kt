package soy.gabimoreno.data.remote.mapper

import org.amshove.kluent.shouldBe
import org.junit.Test
import soy.gabimoreno.data.remote.model.MemberApiModel
import soy.gabimoreno.data.remote.model.StatusApiModel

class MemberMapperKtTest {

    @Test
    fun `GIVEN an MemberApiModel list WHEN toDomain THEN return the expected member`() {
        val memberApiModelList = listOf(buildMemberApiModel())

        val result = memberApiModelList.toDomain()

        result.isActive shouldBe IS_ACTIVE
    }

    private fun buildMemberApiModel() = MemberApiModel(
        statusApiModel = statusApiModel,
        isActive = IS_ACTIVE
    )
}

private const val IS_ACTIVE = true
private val statusApiModel = StatusApiModel.ACTIVE
