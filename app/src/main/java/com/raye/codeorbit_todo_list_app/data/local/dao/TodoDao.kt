package com.raye.codeorbit_todo_list_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raye.codeorbit_todo_list_app.data.local.dao.entity.TodoTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_tasks ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTasks(): Flow<List<TodoTaskEntity>>

    @Query(
        """
        SELECT * FROM todo_tasks
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        ORDER BY isCompleted ASC, createdAt DESC
    """
    )
    fun searchTasks(query: String): Flow<List<TodoTaskEntity>>

    @Query("SELECT * FROM todo_tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<TodoTaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TodoTaskEntity)

    @Update
    suspend fun updateTask(task: TodoTaskEntity)

    @Query("DELETE FROM todo_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

}