package com.easyvax.repository;

import com.easyvax.model.Operatore;
import com.easyvax.model.Personale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * L'interfaccia OperatoreRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

@Repository
public interface OperatoreRepository extends JpaRepository<Operatore, Long> {

    boolean existsByUtente_Id(Long id);

    List<Operatore> findByCentroVaccinale_Id(Long id);

    boolean existsByUtente_IdAndCentroVaccinale(Long id, Long idCvex);

    @Query("select (o) from Operatore o JOIN Utente u on o.utente.id = u.id  where u.codFiscale=:cf")
    Operatore findByCodFisc(@Param("cf") String cf);

    Operatore findByUtente_Id(Long id);

    @Query("select count (o) from Operatore o where o.centroVaccinale.id=:idCentro AND o.id=:idOperatore")
    int checkOperatore(@Param("idCentro") Long idCentro,@Param("idOperatore") Long ioOperatore);

}
