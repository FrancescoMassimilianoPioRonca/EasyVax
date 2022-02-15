package com.easyvax.service.service;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.VaccinoDTO;

import java.util.List;

public interface CentroVaccinaleService {

    CentroVaccinaleDTO findbyName(String nome);
    List<CentroVaccinaleDTO> findAll();
    List<CentroVaccinaleDTO> findByCap(String cap);
    List<CentroVaccinaleDTO> findByVaccino(VaccinoDTO vaccino);

    /**
     * Riserveti agli admins
     * @param centro
     * @return
     */
    CentroVaccinaleDTO insertCentro(CentroVaccinaleDTO centro);
    CentroVaccinaleDTO updateCentro(CentroVaccinaleDTO centro);
    List<CentroVaccinaleDTO> deleteCentro(Long id);
}
