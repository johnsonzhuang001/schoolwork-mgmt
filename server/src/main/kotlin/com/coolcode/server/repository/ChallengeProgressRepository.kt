package com.coolcode.server.repository

import com.coolcode.server.model.ChallengeProgress
import com.coolcode.server.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeProgressRepository: CrudRepository<ChallengeProgress, Long> {
    fun findByChallenger(challenger: User): ChallengeProgress?
}