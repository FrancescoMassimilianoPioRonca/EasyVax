package com.easyvax.repository;

import com.easyvax.model.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegioneRepository extends JpaRepository<Regione,Long> {

    boolean existsByNome(String nome);
    Regione findByNome(String nome);



    @Query("select distinct (r) from Regione r JOIN Provincia p on p.regione.id = p.id  where p.nome=:nome")
    Regione findByProvincia(@Param("nome")String provincia);

}
