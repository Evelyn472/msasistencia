package com.colegio.msasistencia.controller;

import com.colegio.msasistencia.dto.AsistenciaRequestDTO;
import com.colegio.msasistencia.dto.AsistenciaResponseDTO;
import com.colegio.msasistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asistenciaService.guardar(dto));
    }

    @GetMapping("/curso/{cursoId}")
    public List<AsistenciaResponseDTO> buscarPorCursoYFecha(
            @PathVariable Long cursoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return asistenciaService.obtenerPorCursoYFecha(cursoId, fecha);
    }
}