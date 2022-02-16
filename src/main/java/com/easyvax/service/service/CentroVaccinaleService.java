package com.easyvax.service.service;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.DTO.RegioneDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.model.Provincia;

import java.util.List;

public interface CentroVaccinaleService {

    List<CentroVaccinaleDTO> findbyName(String nome);
    List<CentroVaccinaleDTO> findAll();
    List<CentroVaccinaleDTO> findByCap(String cap);
    List<CentroVaccinaleDTO> findByVaccino(Long id);
    List<CentroVaccinaleDTO> findByProvincia(Long id);
    List<CentroVaccinaleDTO> findByRegione(String regione);

    /**
     * Riserveti agli admins
     * @param centro
     * @return
     */
    CentroVaccinaleDTO insertCentro(CentroVaccinaleDTO centro);
    List<CentroVaccinaleDTO> updateCentro(CentroVaccinaleDTO centro);
    List<CentroVaccinaleDTO> deleteCentro(Long id);
}
