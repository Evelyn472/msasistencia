package com.colegio.msasistencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-curso", url = "${ms.curso.url}")
public interface CursoClient {

    @GetMapping("/api/v1/curso/{id}")
    Object obtenerPorId(@PathVariable("id") Long id);
}