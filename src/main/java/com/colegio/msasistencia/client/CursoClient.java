package com.colegio.msasistencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-curso",
        url = "http://localhost:8082/api/v1/cursos"
)
public interface CursoClient {

    @GetMapping("/{id}")
    Object obtenerPorId(@PathVariable("id") Long id);
}