package soy.gabimoreno.data.remote.mapper

import org.amshove.kluent.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import soy.gabimoreno.data.remote.model.StatusApiModel
import soy.gabimoreno.domain.model.login.Status

@RunWith(Parameterized::class)
class StatusMapperKtTest(
    private val statusApiModel: StatusApiModel,
) {
    @Test
    fun `WHEN toDomain THEN get the expected Status`() {
        val result = statusApiModel.toDomain()

        result shouldBe Status.valueOf(statusApiModel.name.uppercase())
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "statusApiModel: {0}")
        fun data(): List<StatusApiModel> =
            listOf(
                StatusApiModel.ACTIVE,
                StatusApiModel.EXPIRED,
                StatusApiModel.CANCELED,
                StatusApiModel.PENDING,
                StatusApiModel.FREE,
            )
    }
}
