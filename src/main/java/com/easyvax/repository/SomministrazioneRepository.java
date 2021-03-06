package com.easyvax.repository;

import com.easyvax.model.Somministrazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * L'interfaccia SomministrazioneRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface SomministrazioneRepository extends JpaRepository<Somministrazione, Long> {

    @Query("select s from Somministrazione s Join Utente u on s.utente.id = u.id where u.codFiscale=:cf")
    List<Somministrazione> findbyUtente(@Param("cf") String codficeFiscale);

    @Query("select count (s) from Somministrazione s Join Utente u on s.utente.id = u.id JOIN Vaccino v on s.vaccino.id = v.id JOIN CentroVaccinale cv on s.centro.id = cv.id where u.id=:idU and v.id=:idV and s.dataSomministrazione=:data")
    int findByUtente_IdAndVaccino_IdAndDataSomministrazione(@Param("idU") Long idU, @Param("idV") Long idV, @Param("data") LocalDate data);

    Somministrazione findByCodiceSomm(String codice);

    boolean existsByCodiceSomm(String cod);

    boolean existsById(Long id);


    @Query("select count(s) from Somministrazione s JOIN Utente u on s.utente.id=u.id WHERE u.id=:id AND s.dataSomministrazione >= :data")
    int checkVaccini(@Param("id") Long id, @Param("data") LocalDate data);


    @Query("select count(s) from Somministrazione s WHERE s.centro.id=:centro AND s.dataSomministrazione=:date")
    int somministrazioniOdierne(@Param("centro") Long id,@Param("date") LocalDate date);

}
