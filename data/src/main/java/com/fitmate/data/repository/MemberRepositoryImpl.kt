package com.fitmate.data.repository

import com.fitmate.data.remote.api.MemberApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.MemberResponseDto
import com.fitmate.data.remote.dto.WorkoutEnvironmentResponseDto
import com.fitmate.data.remote.toEnumOrNull
import com.fitmate.domain.model.ExerciseLevel
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.GoalStrategy
import com.fitmate.domain.model.MemberInfo
import com.fitmate.domain.model.PrimaryGoal
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.domain.model.WorkoutEnvironment
import com.fitmate.domain.model.WorkoutEnvironments
import com.fitmate.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberApi: MemberApi
) : MemberRepository {

    override suspend fun getMyInfo(): Result<MemberInfo> = apiCall {
        memberApi.getMe().toDomain()
    }

    override suspend fun getWorkoutEnvironments(): Result<WorkoutEnvironments> = apiCall {
        val response = memberApi.getWorkoutEnvironments()
        WorkoutEnvironments(
            primaryLocation = response.primaryLocation.toEnumOrNull<ExerciseLocation>() ?: ExerciseLocation.GYM,
            environments = response.environments.mapNotNull { it.toDomain() }
        )
    }
}

// 회원가입 때 서버 enum으로 저장한 값이라 모르는 값이 오면 서버 명세가 바뀐 것. 오류로 처리한다
private fun MemberResponseDto.toDomain() = MemberInfo(
    height = height,
    weight = weight,
    exerciseLevel = checkNotNull(exerciseLevel.toEnumOrNull<ExerciseLevel>()),
    primaryGoal = checkNotNull(primaryGoal.toEnumOrNull<PrimaryGoal>()),
    goalStrategy = checkNotNull(goalStrategy.toEnumOrNull<GoalStrategy>()),
    sessionMinutes = sessionMinutes,
    availableDays = availableDays.mapNotNull { it.toEnumOrNull<WorkoutDay>() }.toSet(),
    exerciseLocation = checkNotNull(exerciseLocation.toEnumOrNull<ExerciseLocation>())
)

private fun WorkoutEnvironmentResponseDto.toDomain(): WorkoutEnvironment? {
    return WorkoutEnvironment(
        id = id,
        location = locationType.toEnumOrNull<ExerciseLocation>() ?: return null,
        gymName = gymName,
        equipmentCodes = equipmentCodes
    )
}
