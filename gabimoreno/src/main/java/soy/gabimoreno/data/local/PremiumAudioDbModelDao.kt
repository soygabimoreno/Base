package soy.gabimoreno.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PremiumAudioDbModelDao {
    @Query("SELECT * FROM PremiumAudioDbModel")
    fun getPremiumAudioDbModels(): List<PremiumAudioDbModel>

    @Query("SELECT COUNT(id) FROM PremiumAudioDbModel")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPremiumAudioDbModels(premiumAudioDbModels: List<PremiumAudioDbModel>)

    @Query("DELETE FROM PremiumAudioDbModel")
    fun deleteAllPremiumAudioDbModels()
}
