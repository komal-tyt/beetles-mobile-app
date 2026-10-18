package data

import kotlinx.coroutines.flow.Flow
import com.example.beetles_app.data.Score

class ScoreRepository(private val scoreDao: ScoreDao) {

    fun getAllScores(): Flow<List<Score>> = scoreDao.getAllScores()

    suspend fun insertScore(score: Score) = scoreDao.insert(score)
}