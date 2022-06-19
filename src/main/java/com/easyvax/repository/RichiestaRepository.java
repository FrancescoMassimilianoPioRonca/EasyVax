package com.easyvax.repository;

import com.easyvax.model.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


/**
 * L'interfaccia RichiestaRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */
public interface RichiestaRepository extends JpaRepository<Richiesta, Long> {

    @Query("select distinct(r) from Richiesta r WHERE r.id IN (select r from Richiesta where r.newCentro=:id OR r.oldCentroVacc=:id) AND r.approved is null ")
    List<Richiesta> getRichieste(@Param("id") Long id);

    @Query("select distinct(r) from Richiesta r  WHERE r.somministrazione.utente.id=:cod AND r.approved is NULL ")
    List<Richiesta> getRichiesteUtente(@Param("cod") Long cod);

    boolean existsById(Long id);


    @Query("select count(r) from Richiesta r JOIN Somministrazione s On r.somministrazione.id=:id WHERE r.approved is NULL")
    int findBySomministrazione_IdAndApproved(@Param("id") Long id);

    Richiesta findBySomministrazione_Id(Long id);


}
