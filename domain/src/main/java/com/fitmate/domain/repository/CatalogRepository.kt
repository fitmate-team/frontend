package com.fitmate.domain.repository

import com.fitmate.domain.model.Equipment
import com.fitmate.domain.model.Exercise
import com.fitmate.domain.model.ExerciseLocation

// 운동 기구 / 운동 목록 조회 (로그인 없이 호출 가능)
interface CatalogRepository {

    suspend fun getEquipments(location: ExerciseLocation): Result<List<Equipment>>

    suspend fun getExercises(): Result<List<Exercise>>
}
