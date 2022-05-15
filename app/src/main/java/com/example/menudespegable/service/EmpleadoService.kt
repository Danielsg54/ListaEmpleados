package com.example.menudespegable.service

import com.example.menudespegable.entity.Empleado
import com.example.menudespegable.entity.Record
import retrofit2.Call
import retrofit2.http.*

interface EmpleadoService {

    @GET ("empleado")
    fun getEmpleado() : Call<Record>


    @PUT("empleado/{idEmpleado}")
    fun update (@Path("idEmpleado") idEmpleado: Int, @Body empleado: Empleado) : Call<String>

    @POST("empleado")
    fun create(@Body empleado: Empleado): Call<String>

    @DELETE("empleado/{idEmpleado}")
    fun delete (@Path("idEmpleado") idEmpleado: Int): Call<String>
}