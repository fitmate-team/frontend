package com.fitmate.domain.repository

import com.fitmate.domain.model.MemberInfo
import com.fitmate.domain.model.WorkoutEnvironments

// 내 정보 / 운동 환경 조회
interface MemberRepository {

    suspend fun getMyInfo(): Result<MemberInfo>

    suspend fun getWorkoutEnvironments(): Result<WorkoutEnvironments>
}
