package com.example.cafe.data

import android.content.Context
import androidx.room.*

// Entity
@Entity(tableName = "dialog_progress")
data class DialogProgress(
    @PrimaryKey val dialogId: String,
    val currentLine: Int
)

// DAO
// DAO - БЕЗ suspend
@Dao
interface DialogDao {
    @Query("SELECT * FROM dialog_progress WHERE dialogId = :dialogId")
    fun get(dialogId: String): DialogProgress?  // ← убрал suspend

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(progress: DialogProgress)  // ← убрал suspend
}

// Database
@Database(entities = [DialogProgress::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dialogDao(): DialogDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cafe.db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}