package com.easyvax.service.service;

import com.easyvax.dto.CentroVaccinaleDTO;

import java.util.List;

public interface CentroVaccinaleService {

    List<CentroVaccinaleDTO> findbyName(String nome);

    List<CentroVaccinaleDTO> findAll();

    List<CentroVaccinaleDTO> findByCap(String cap);

    List<CentroVaccinaleDTO> findByProvincia(Long id);

    List<CentroVaccinaleDTO> findByRegione(String regione);


    CentroVaccinaleDTO insertCentro(CentroVaccinaleDTO centro);

    List<CentroVaccinaleDTO> updateCentro(CentroVaccinaleDTO centro);

    boolean deleteCentro(Long id);
}
