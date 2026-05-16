package com.colegio.msasistencia.service;

import com.colegio.msasistencia.client.CursoClient;
import com.colegio.msasistencia.client.EstudianteClient;
import com.colegio.msasistencia.dto.AsistenciaRequestDTO;
import com.colegio.msasistencia.dto.AsistenciaResponseDTO;
import com.colegio.msasistencia.model.Asistencia;
import com.colegio.msasistencia.repository.AsistenciaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EstudianteClient estudianteClient;
    private final CursoClient cursoClient;

    public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto) {

        estudianteClient.obtenerPorId(dto.getEstudianteId());
        cursoClient.obtenerPorId(dto.getCursoId());

        Asistencia asistencia = new Asistencia();

        asistencia.setEstudianteId(dto.getEstudianteId());
        asistencia.setCursoId(dto.getCursoId());
        asistencia.setFecha(dto.getFecha());
        asistencia.setEstado(dto.getEstado());

        Asistencia guardada = asistenciaRepository.save(asistencia);

        return convertirDTO(guardada);
    }

    public List<AsistenciaResponseDTO> obtenerPorCursoYFecha(Long cursoId, LocalDate fecha) {

        return asistenciaRepository
                .findByCursoIdAndFecha(cursoId, fecha)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private AsistenciaResponseDTO convertirDTO(Asistencia asistencia) {

        return new AsistenciaResponseDTO(
                asistencia.getId(),
                asistencia.getEstudianteId(),
                asistencia.getCursoId(),
                asistencia.getFecha(),
                asistencia.getEstado()
        );
    }
}