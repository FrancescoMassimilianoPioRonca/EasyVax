package com.easyvax.service.service;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.TurnoDTO;
import com.easyvax.model.CentroVaccinale;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {

    /**
     * Admin
     * @param turno
     * @return
     */
    TurnoDTO insertTurno(TurnoDTO turno);
    List<TurnoDTO> deleteturno(Long id);
    TurnoDTO updateTurno(TurnoDTO turno);
    List<TurnoDTO> findByDate(LocalDate data);
    List<TurnoDTO> findByDateAndOra(LocalDate data, String ora);
    List<TurnoDTO> findByCentroVaccinale(CentroVaccinaleDTO centro);
    List<TurnoDTO> findByRuolo(String ruolo);

    /**
     * Anche user
     * @param ruolo
     * @param centro
     * @return
     */
    List<TurnoDTO> findByRuoloAndCentroVaccinale(String ruolo, CentroVaccinaleDTO centro);

}
