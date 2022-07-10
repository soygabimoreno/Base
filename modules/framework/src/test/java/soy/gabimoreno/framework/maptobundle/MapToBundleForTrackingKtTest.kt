package soy.gabimoreno.framework.maptobundle

import android.os.Bundle
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapToBundleForTrackingKtTest {

    @Test
    fun `GIVEN a map WHEN mapToBundleForTracking THEN get the expected bundle`() {
        val key1 = "key1"
        val key2 = "key2"
        val key3 = "key3"
        val key4 = "key4"
        val key5 = "key5"

        val value1 = 1
        val value2 = "string"
        val value3 = 1.0
        val value4 = 1f
        val value5 = true

        val map = mapOf(
            key1 to value1,
            key2 to value2,
            key3 to value3,
            key4 to value4,
            key5 to value5
        )

        val result = map.mapToBundleForTracking()

        val bundle = Bundle().apply {
            putString(key1, value1.toString())
            putString(key2, value2)
            putString(key3, value3.toString())
            putString(key4, value4.toString())
            putString(key5, value5.toString())
        }

        result[key1] shouldBeEqualTo bundle[key1]
        result[key2] shouldBeEqualTo bundle[key2]
        result[key3] shouldBeEqualTo bundle[key3]
        result[key4] shouldBeEqualTo bundle[key4]
        result[key5] shouldBeEqualTo bundle[key5]
    }
}
