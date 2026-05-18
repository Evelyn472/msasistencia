package com.colegio.msasistencia.repository;

import com.colegio.msasistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEstudianteId(Long estudianteId);

    List<Asistencia> findByCursoId(Long cursoId);
}