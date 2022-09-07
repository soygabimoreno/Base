package soy.gabimoreno.core.testing

import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify

inline fun <reified T : Any> relaxedMockk(): T = mockk(relaxed = true)

fun verifyNever(
    verifyBlock: MockKVerificationScope.() -> Unit,
) = verify(
    exactly = 0,
    verifyBlock = verifyBlock
)

fun verifyOnce(
    verifyBlock: MockKVerificationScope.() -> Unit,
) = verify(
    exactly = 1,
    verifyBlock = verifyBlock
)

fun coVerifyNever(
    verifyBlock: suspend MockKVerificationScope.() -> Unit,
) = coVerify(
    exactly = 0,
    verifyBlock = verifyBlock
)

fun coVerifyOnce(
    verifyBlock: suspend MockKVerificationScope.() -> Unit,
) = coVerify(
    exactly = 1,
    verifyBlock = verifyBlock
)
