package com.easyvax.repository;

import com.easyvax.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinciaRepository extends JpaRepository<Provincia,Long> {

    boolean existsByNome(String nome);
    List<Provincia> findByNome(String nome);
    boolean existsByCap(String cap);
    boolean existsByNomeAndRegione_IdAndCap(String nome, Long id, String cap);
    boolean existsByNomeAndCap(String nome, String cap);

    List<Provincia> findByRegione_Id(Long id);

    Provincia findByCap(String cap);

}
