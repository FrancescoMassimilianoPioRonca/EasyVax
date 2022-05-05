package com.easyvax.repository;

import com.easyvax.model.CentroVaccinale;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



/**
 * L'interfaccia CentroVaccinaleRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */


@Repository
public interface CentroVaccinaleRepository extends JpaRepository<CentroVaccinale,Long> {

    boolean existsByNome(String nome);

    List<CentroVaccinale> findAll();

    List<CentroVaccinale> findByNome(String nome);

    List<CentroVaccinale> findByProvincia_Id(Long id);

    boolean existsByNomeAndIndirizzoAndProvincia_Id(String nome, String indirizzo, Long id);


    @Query("select distinct (c) from CentroVaccinale c JOIN Provincia p on c.provincia = p JOIN Regione r on p.regione.id = r.id where r.nome=:id")
    List<CentroVaccinale> findByRegione(@Param("id") String nome);

    boolean existsByNomeAndProvincia_Id(@NonNull String nome, Long id);


    @Query("select distinct c from CentroVaccinale c JOIN Provincia p on c.provincia.id = p.id  where p.cap=:cap")
    List<CentroVaccinale> findByCap(@Param("cap")String cap);


    @Query("select distinct c from Somministrazione s JOIN CentroVaccinale c on s.centro.id = c.id  where s.vaccino.id=:id_vaccino")
    List<CentroVaccinale> findByVaccino(@Param("id_vaccino")Long id);

}
