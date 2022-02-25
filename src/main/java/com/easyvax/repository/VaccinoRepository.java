package com.easyvax.repository;

import com.easyvax.model.Vaccino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinoRepository extends JpaRepository<Vaccino,Long> {

    List<Vaccino>  findVaccinoByCasaFarmaceutica(String casaFarmaceutica);
    List<Vaccino> findByCasaFarmaceutica(String casaFarmaceutica);
    Vaccino findByNome (String nome);

    boolean existsByNome(String nome);
    boolean existsByNomeAndCasaFarmaceutica(String nome, String casaFarmaceutica);
    boolean existsById(Long id);
    boolean existsByCasaFarmaceutica(String casaFarmaceutica);
}
