package com.easyvax.repository;

import com.easyvax.model.Personale;
import com.easyvax.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaleRepository extends JpaRepository<Personale,Long> {
}
