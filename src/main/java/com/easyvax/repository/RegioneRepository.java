package com.easyvax.repository;

import com.easyvax.model.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * L'interfaccia RegioneRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */
public interface RegioneRepository extends JpaRepository<Regione, Long> {

    boolean existsByNome(String nome);

    Regione findByNome(String nome);


    @Query("select distinct (r) from Regione r JOIN Provincia p on p.regione.id = r.id  where p.nome=:nome")
    List<Regione> findByProvincia(@Param("nome") String provincia);

}
