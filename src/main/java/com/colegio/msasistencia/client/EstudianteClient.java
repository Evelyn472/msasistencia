package com.colegio.msasistencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-estudiantes",
        url = "http://localhost:8081/api/v1/estudiantes"
)
public interface EstudianteClient {

    @GetMapping("/{id}")
    Object obtenerPorId(@PathVariable("id") Long id);
}