package com.easyvax.repository;

import com.easyvax.model.Vaccino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * L'interfaccia VaccinoRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */
public interface VaccinoRepository extends JpaRepository<Vaccino,Long> {

    List<Vaccino>  findVaccinoByCasaFarmaceutica(String casaFarmaceutica);
    List<Vaccino> findByCasaFarmaceutica(String casaFarmaceutica);
    Vaccino findByNome (String nome);

    boolean existsByNome(String nome);
    boolean existsByNomeAndCasaFarmaceutica(String nome, String casaFarmaceutica);
    boolean existsById(Long id);
    //boolean existsByCasaFarmaceutica(String casaFarmaceutica);
}
