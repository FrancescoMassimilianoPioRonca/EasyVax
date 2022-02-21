package com.easyvax.repository;

import com.easyvax.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente,Long> {

    boolean existsByCognome(String cognome);
    boolean existsByCodFiscale(String cf);
    boolean existsByEmail(String email);

    boolean existsByNomeAndCognomeAndCodFiscaleAndDataNascitaAndRuolo(String nome, String cognome, String cf, LocalDate data, String ruolo);

    List<Utente> findByCognome(String cognome);
    Utente findByCodFiscale(String cf);


    @Query("select distinct u from Utente u JOIN Provincia p on u.provincia.id=p.id where p.cap=:cap")
    List<Utente> findByCap(@Param("cap") String cap);
}
