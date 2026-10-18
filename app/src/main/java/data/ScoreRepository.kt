package com.example.beetles_app.data

import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {

    fun getAllScores(): Flow<List<Score>> = scoreDao.getAllScores()

    suspend fun insertScore(score: Score) = scoreDao.insert(score)

    suspend fun clearAllScores() = scoreDao.deleteAll()

    suspend fun saveBestScore(score: Score) {
        val existing = scoreDao.getByName(score.firstName, score.lastName)
        if (existing == null || score.score > existing.score) {
            scoreDao.deleteByName(score.firstName, score.lastName)
            scoreDao.insert(score)
        }
    }
}