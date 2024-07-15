package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.ChallengeProgress
import com.schoolwork.mgmt.server.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeProgressRepository: CrudRepository<ChallengeProgress, Long> {
    fun findByChallenger(challenger: User): ChallengeProgress?
}