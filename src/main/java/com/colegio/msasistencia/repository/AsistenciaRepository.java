package com.colegio.msasistencia.repository;

import com.colegio.msasistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByCursoIdAndFecha(Long cursoId, LocalDate fecha);
}