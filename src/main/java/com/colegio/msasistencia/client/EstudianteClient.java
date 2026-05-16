package com.colegio.msasistencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-estudiantes", url = "${ms.estudiantes.url}")
public interface EstudianteClient {

    @GetMapping("/api/v1/estudiantes/{id}")
    Object obtenerPorId(@PathVariable("id") Long id);
}