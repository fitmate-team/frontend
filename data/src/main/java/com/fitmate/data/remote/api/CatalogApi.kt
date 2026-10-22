package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.EquipmentResponseDto
import com.fitmate.data.remote.dto.ExerciseResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogApi {

    // location: GYM / HOME / OUTDOOR
    @GET("api/equipments")
    suspend fun getEquipments(@Query("location") location: String): List<EquipmentResponseDto>

    // keyword, muscle을 비우면 전체 목록
    @GET("api/exercises")
    suspend fun getExercises(
        @Query("keyword") keyword: String? = null,
        @Query("muscle") muscle: String? = null
    ): List<ExerciseResponseDto>
}
