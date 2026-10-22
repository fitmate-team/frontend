package com.fitmate.data.repository

import com.fitmate.data.local.TokenStorage
import com.fitmate.data.remote.api.AuthApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.LoginRequestDto
import com.fitmate.data.remote.dto.RecentExerciseRecordRequestDto
import com.fitmate.data.remote.dto.SignUpRequestDto
import com.fitmate.domain.model.SignUpForm
import com.fitmate.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(loginId: String, password: String): Result<Unit> = apiCall {
        val response = authApi.login(LoginRequestDto(loginId, password))
        tokenStorage.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun isLoginIdAvailable(loginId: String): Result<Boolean> = apiCall {
        authApi.checkLoginId(loginId).available
    }

    override suspend fun signUp(form: SignUpForm): Result<Unit> = apiCall {
        authApi.signUp(form.toDto())
        Unit
    }
}

private fun SignUpForm.toDto() = SignUpRequestDto(
    loginId = loginId,
    password = password,
    gender = gender.name,
    age = age,
    height = height,
    weight = weight,
    skeletalMuscleMass = skeletalMuscleMass,
    bodyFatPercentage = bodyFatPercentage,
    bodyFatMass = bodyFatMass,
    exerciseLevel = exerciseLevel.name,
    currentExerciseStatus = currentExerciseStatus.name,
    primaryGoal = primaryGoal.name,
    goalStrategy = goalStrategy.name,
    sessionMinutes = sessionMinutes,
    availableDays = availableDays.sorted().map { it.name },
    exerciseLocation = exerciseLocation.name,
    gymName = gymName,
    equipmentCodes = equipmentCodes.toList(),
    avoidBodyAreas = avoidBodyAreas.map { it.name },
    excludedExerciseCodes = excludedExerciseCodes.toList(),
    recentExerciseRecords = recentExerciseRecords.map {
        RecentExerciseRecordRequestDto(
            exerciseCode = it.exerciseCode,
            weight = it.weight,
            reps = it.reps,
            sets = it.sets,
            durationSeconds = it.durationSeconds
        )
    }
)
