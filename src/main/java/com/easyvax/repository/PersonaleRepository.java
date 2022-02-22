package com.easyvax.repository;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import com.easyvax.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonaleRepository extends JpaRepository<Personale,Long> {

    boolean existsByUtente_Id(Long id);

    List<Personale> findByCentroVaccinale_Id(Long id);

    boolean existsByUtente_IdAndCentroVaccinale(Long id,Long idCvex);

    @Query("select distinct (p) from Personale p JOIN Utente u on p.utente.id = u.id  where u.cognome=:cognome")
    List<Personale> findByCognome(@Param("cognome") String cognome);

    @Query("select (p) from Personale p JOIN Utente u on p.utente.id = u.id  where u.codFiscale=:cf")
    Personale findByCodFisc(@Param("cf") String cf);
}
