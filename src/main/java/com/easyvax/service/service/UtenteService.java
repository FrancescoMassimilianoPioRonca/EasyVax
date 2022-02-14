package com.easyvax.service.service;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.UtenteDTO;

import java.util.List;

public interface UtenteService {

    UtenteDTO insertUtente(UtenteDTO utente);

    UtenteDTO getDetails(Long id);

    UtenteDTO updateAnagrafica(UtenteDTO utente);

    List<UtenteDTO> findAll();

    List<UtenteDTO> finByCap();

    UtenteDTO findByCognome(String cognome);

    UtenteDTO findById(Long Id);

    /**
     * Riservato ad admin
     * @param id
     * @return
     */
    List<UtenteDTO> deleteUtente(Long id);
    List<UtenteDTO> findByAdmin();
}
