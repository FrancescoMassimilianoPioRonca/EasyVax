package com.easyvax.repository;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Regione;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
