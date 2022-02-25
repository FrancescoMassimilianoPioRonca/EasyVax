package com.easyvax.repository;

import com.easyvax.model.Somministrazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SomministrazioneRepository extends JpaRepository<Somministrazione,Long> {

    @Query("select s from Somministrazione s Join Utente u on s.utente.id = u.id where u.codFiscale=:cf")
    List<Somministrazione> findbyUtente(@Param("cf") String codficeFiscale);

    @Query("select s from Somministrazione s Join Utente u on s.utente.id = u.id where u.codFiscale=:cf and s.dataSomministrazione=:data")
    List<Somministrazione> findbyUtenteAndData(@Param("cf") String codficeFiscale, @Param("data") LocalDate data);


    @Query("select count (s) from Somministrazione s Join Utente u on s.utente.id = u.id JOIN Vaccino v on s.vaccino.id = v.id JOIN CentroVaccinale cv on s.centro.id = cv.id where u.id=:idU and v.id=:idV and s.dataSomministrazione=:data")
    int findByUtente_IdAndVaccino_IdAndDataSomministrazione(@Param("idU")Long idU, @Param("idV")Long idV, @Param("data")LocalDate data);

    Somministrazione findByCodiceSomm(String codice);

    boolean existsByCodiceSomm(String cod);



}
