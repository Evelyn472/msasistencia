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
                a.getId(),
                a.getEstudianteId(),
                a.getCursoId(),
                a.getFecha(),
                a.getEstado()
        );
    }

    private void validarEstudiante(Long estudianteId) {

        if (estudianteId == null) return;

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

    private void validarCurso(Long cursoId) {

        if (cursoId == null) return;

        try {

            cursoClient.obtenerPorId(cursoId);

            log.info(">>> Curso {} validado correctamente (FeignClient)", cursoId);

        } catch (FeignException.NotFound e) {

            throw new RuntimeException(
                    "El curso con id " + cursoId + " no existe en ms-cursos.");

        } catch (FeignException e) {

            throw new RuntimeException(
                    "No se puede conectar con ms-cursos: " + e.getMessage());
        }
    }

    public List<AsistenciaResponseDTO> obtenerTodos() {

        return asistenciaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AsistenciaResponseDTO> obtenerPorId(Long id) {

        return asistenciaRepository.findById(id)
                .map(this::mapToDTO);
    }

    public List<AsistenciaResponseDTO> obtenerPorEstudiante(Long estudianteId) {

        return asistenciaRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> obtenerPorCurso(Long cursoId) {

        return asistenciaRepository.findByCursoId(cursoId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto) {

        validarEstudiante(dto.getEstudianteId());

        validarCurso(dto.getCursoId());

        Asistencia a = new Asistencia(
                null,
                dto.getEstudianteId(),
                dto.getCursoId(),
                dto.getFecha(),
                dto.getEstado()
        );

        return mapToDTO(asistenciaRepository.save(a));
    }

    public Optional<AsistenciaResponseDTO> actualizar(Long id, AsistenciaRequestDTO dto) {

        return asistenciaRepository.findById(id).map(existente -> {

            validarEstudiante(dto.getEstudianteId());

            validarCurso(dto.getCursoId());

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