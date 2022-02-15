package com.easyvax.repository;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroVaccinaleRepository extends JpaRepository<CentroVaccinale,Long> {

    boolean existsByNome(String nome);
    CentroVaccinaleDTO findByNome(String nome);

    boolean existsByCap(String cap);
    List<CentroVaccinaleDTO> findByCap(String cap);
}
