package com.colegio.msasistencia.service;

import com.colegio.msasistencia.client.CursoClient;
import com.colegio.msasistencia.client.EstudianteClient;
import com.colegio.msasistencia.dto.AsistenciaRequestDTO;
import com.colegio.msasistencia.dto.AsistenciaResponseDTO;
import com.colegio.msasistencia.model.Asistencia;
import com.colegio.msasistencia.repository.AsistenciaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private EstudianteClient estudianteClient;

    @Mock
    private CursoClient cursoClient;

    @InjectMocks
    private AsistenciaService asistenciaService;

   @Test
    void guardarAsistenciaCorrectamente() {

    AsistenciaRequestDTO dto = new AsistenciaRequestDTO(
            1L,
            1L,
            LocalDate.now(),
            "PRESENTE"
    );

    Asistencia asistenciaGuardada = new Asistencia(
            1L,
            1L,
            1L,
            LocalDate.now(),
            "PRESENTE"
    );

    when(estudianteClient.obtenerPorId(1L))
            .thenReturn(new Object());

    when(cursoClient.obtenerPorId(1L))
            .thenReturn(new Object());

    when(asistenciaRepository.save(any(Asistencia.class)))
            .thenReturn(asistenciaGuardada);

    AsistenciaResponseDTO resultado = asistenciaService.guardar(dto);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals("PRESENTE", resultado.getEstado());

    verify(asistenciaRepository, times(1))
            .save(any(Asistencia.class));
    }

    @Test
    void obtenerAsistenciaPorId() {

    Asistencia asistencia = new Asistencia(
            1L,
            1L,
            1L,
            LocalDate.now(),
            "PRESENTE"
    );

    when(asistenciaRepository.findById(1L))
            .thenReturn(Optional.of(asistencia));

    Optional<AsistenciaResponseDTO> resultado =
            asistenciaService.obtenerPorId(1L);

    assertTrue(resultado.isPresent());
    assertEquals(1L, resultado.get().getId());

    verify(asistenciaRepository, times(1))
            .findById(1L);
    }

    @Test
    void eliminarAsistencia() {

        doNothing().when(asistenciaRepository)
                .deleteById(1L);

        asistenciaService.eliminar(1L);

        verify(asistenciaRepository, times(1))
                .deleteById(1L);
    }
}