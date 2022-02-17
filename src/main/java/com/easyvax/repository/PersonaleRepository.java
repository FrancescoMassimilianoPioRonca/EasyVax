package com.easyvax.repository;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import com.easyvax.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaleRepository extends JpaRepository<Personale,Long> {

    @Query("select distinct p from Personale p JOIN CentroVaccinale cv JOIN Provincia pr on cv.provincia.id = pr.id  where pr.cap=:cap")
    List<Personale> findByCap(@Param("cap")String cap);

    boolean existsByCognome(String cognome);

    boolean existsByRuolo(String ruolo);

    List<Personale> findByCognome(String cognome);

    List<Personale> findByRuolo(String ruolo);


}
