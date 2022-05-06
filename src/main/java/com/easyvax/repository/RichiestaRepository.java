package com.easyvax.repository;

import com.easyvax.model.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * L'interfaccia RichiestaRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */
public interface RichiestaRepository extends JpaRepository<Richiesta, Long> {

    @Query("select distinct(r) from Richiesta r JOIN Somministrazione s On r.somministrazione.id=s.id JOIN CentroVaccinale cv ON cv.id=s.centro.id JOIN Operatore o ON o.centroVaccinale.id=r.IdCentroVacc OR o.centroVaccinale.id=s.centro.id WHERE  r.approved is NULL AND r.approvedOp2 is NULL AND o.id=:id")
    List<Richiesta> getRichieste(@Param("id") Long id);

    @Query("select distinct(r) from Richiesta r JOIN Somministrazione s On r.somministrazione.id=s.id WHERE s.utente.id=:cod AND r.approved is NULL")
    List<Richiesta> getRichiesteUtente(@Param("cod") Long cod);

    boolean existsById(Long id);

    boolean existsBySomministrazione_Id(Long id);

}
