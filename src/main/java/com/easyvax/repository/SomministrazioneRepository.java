package com.easyvax.repository;

import com.easyvax.model.Personale;
import com.easyvax.model.Somministrazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SomministrazioneRepository extends JpaRepository<Somministrazione,Long> {
}
