package com.easyvax.repository;

import com.easyvax.model.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RichiestaRepository  extends JpaRepository<Richiesta,Long> {

    @Query("select distinct(r) from Richiesta r JOIN Somministrazione s On r.somministrazione.id=s.id WHERE s.centro.id=:idA AND r.approved is NULL ")
    List<Richiesta>getRichiestePersonal(@Param("idA")Long id);

    @Query("select distinct(r) from Richiesta r JOIN Somministrazione s On r.somministrazione.id=s.id WHERE s.utente.id=:cod AND r.approved is NULL")
    List<Richiesta>getRichiesteUtente(@Param("cod")Long cod);

    boolean existsById(Long id);

}
