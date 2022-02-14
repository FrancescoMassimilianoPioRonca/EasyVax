package com.easyvax.repository;

import com.easyvax.model.Somministrazione;
import com.easyvax.model.Vaccino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinoRepository extends JpaRepository<Vaccino,Long> {
}
