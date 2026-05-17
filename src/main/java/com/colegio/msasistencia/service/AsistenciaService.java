package com.colegio.msasistencia.service;

import com.colegio.msasistencia.client.CursoClient;
import com.colegio.msasistencia.client.EstudianteClient;
import com.colegio.msasistencia.dto.AsistenciaRequestDTO;
import com.colegio.msasistencia.dto.AsistenciaResponseDTO;
import com.colegio.msasistencia.model.Asistencia;
import com.colegio.msasistencia.repository.AsistenciaRepository;


import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    private final EstudianteClient estudianteClient;
    private final CursoClient cursoClient;

    private AsistenciaResponseDTO mapToDTO(Asistencia a) {
        return new AsistenciaResponseDTO(
                a.getId(), a.getEstudianteId(), a.getCursoId(), a.getFecha(), a.getEstado());
    }

    private void validarDatosAsistencia(Long estudianteId, Long cursoId) {
        if (estudianteId != null) {
            try {
                estudianteClient.obtenerPorId(estudianteId);
                log.info(">>> Estudiante {} validado correctamente (FeignClient)", estudianteId);
            } catch (FeignException.NotFound e) {
                throw new RuntimeException(
                        "El estudiante con id " + estudianteId + " no existe en ms-estudiantes.");
            } catch (FeignException e) {
                throw new RuntimeException(
                        "No se puede conectar con ms-estudiantes: " + e.getMessage());
            }
        }

        if (cursoId != null) {
            try {
                cursoClient.obtenerPorId(cursoId);
                log.info(">>> Curso {} validado correctamente (FeignClient)", cursoId);
            } catch (FeignException.NotFound e) {
                throw new RuntimeException(
                        "El curso con id " + cursoId + " no existe en ms-curso.");
            } catch (FeignException e) {
                throw new RuntimeException(
                        "No se puede conectar con ms-curso: " + e.getMessage());
            }
        }
    }

    public List<AsistenciaResponseDTO> obtenerTodas() {
        return asistenciaRepository.findAll().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<AsistenciaResponseDTO> obtenerPorId(Long id) {
        return asistenciaRepository.findById(id).map(this::mapToDTO);
    }

    public List<AsistenciaResponseDTO> obtenerPorCursoYFecha(Long cursoId, LocalDate fecha) {
        return asistenciaRepository.findByCursoIdAndFecha(cursoId, fecha).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto) {
        validarDatosAsistencia(dto.getEstudianteId(), dto.getCursoId());
        
        Asistencia a = new Asistencia(null, dto.getEstudianteId(), dto.getCursoId(), dto.getFecha(), dto.getEstado());
        return mapToDTO(asistenciaRepository.save(a));
    }

    public Optional<AsistenciaResponseDTO> actualizar(Long id, AsistenciaRequestDTO dto) {
        return asistenciaRepository.findById(id).map(existente -> {
            validarDatosAsistencia(dto.getEstudianteId(), dto.getCursoId());
            
            existente.setEstudianteId(dto.getEstudianteId());
            existente.setCursoId(dto.getCursoId());
            existente.setFecha(dto.getFecha());
            existente.setEstado(dto.getEstado());
            return mapToDTO(asistenciaRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }
}