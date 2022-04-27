package com.easyvax.repository;

import com.easyvax.model.Operatore;
import com.easyvax.model.Personale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatoreRepository extends JpaRepository<Operatore,Long> {

    boolean existsByUtente_Id(Long id);

    List<Operatore> findByCentroVaccinale_Id(Long id);

    boolean existsByUtente_IdAndCentroVaccinale(Long id, Long idCvex);

    @Query("select (o) from Operatore o JOIN Utente u on o.utente.id = u.id  where u.codFiscale=:cf")
    Operatore findByCodFisc(@Param("cf") String cf);

}