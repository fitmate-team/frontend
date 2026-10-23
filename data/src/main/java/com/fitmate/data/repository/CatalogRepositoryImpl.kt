package com.fitmate.data.repository

import com.fitmate.data.remote.api.CatalogApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.EquipmentResponseDto
import com.fitmate.data.remote.dto.ExerciseResponseDto
import com.fitmate.domain.model.BaselineRecordType
import com.fitmate.domain.model.Equipment
import com.fitmate.domain.model.EquipmentCategory
import com.fitmate.domain.model.Exercise
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.ExerciseType
import com.fitmate.domain.model.MuscleGroup
import com.fitmate.domain.repository.CatalogRepository
import javax.inject.Inject
import javax.inject.Singleton

// 기구 / 운동 목록은 자주 바뀌지 않으니 앱 실행 동안 메모리에 캐시한다
@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi
) : CatalogRepository {

    private val equipmentCache = mutableMapOf<ExerciseLocation, List<Equipment>>()
    private var exerciseCache: List<Exercise>? = null

    override suspend fun getEquipments(location: ExerciseLocation): Result<List<Equipment>> {
        equipmentCache[location]?.let { return Result.success(it) }
        return apiCall {
            catalogApi.getEquipments(location.name).mapNotNull { it.toDomain() }
        }.onSuccess { equipmentCache[location] = it }
    }

    override suspend fun getExercises(): Result<List<Exercise>> {
        exerciseCache?.let { return Result.success(it) }
        return apiCall {
            catalogApi.getExercises().mapNotNull { it.toDomain() }
        }.onSuccess { exerciseCache = it }
    }
}

// 앱이 모르는 enum 값이 오면 그 항목만 건너뛴다
private inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
    enumValues<T>().firstOrNull { it.name == this }

private fun EquipmentResponseDto.toDomain(): Equipment? {
    return Equipment(
        code = equipmentCode,
        name = nameKo,
        category = category.toEnumOrNull<EquipmentCategory>() ?: return null
    )
}

private fun ExerciseResponseDto.toDomain(): Exercise? {
    return Exercise(
        code = exerciseCode,
        name = nameKo,
        type = exerciseType.toEnumOrNull<ExerciseType>() ?: return null,
        recordType = baselineRecordType.toEnumOrNull<BaselineRecordType>() ?: return null,
        primaryMuscle = primaryMuscle.toEnumOrNull<MuscleGroup>() ?: return null,
        equipmentCategories = equipmentCategories.mapNotNull { it.toEnumOrNull<EquipmentCategory>() }.toSet()
    )
}
