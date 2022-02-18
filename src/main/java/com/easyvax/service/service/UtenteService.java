package com.easyvax.service.service;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.UtenteDTO;

import java.util.List;

public interface UtenteService {

    UtenteDTO insertUtente(UtenteDTO utente);

    UtenteDTO getDetails(Long id);

    List<UtenteDTO> updateAnagrafica(UtenteDTO utente);

    List<UtenteDTO> findAll();

    /*
    List<UtenteDTO> finByCap(String cap);

    List<UtenteDTO>  findByCognome(String cognome);

    List<UtenteDTO>  findByPersonale(Boolean personale);
*/
    /**
     * Riservato ad admin
     * @param id
     * @return
     */
    List<UtenteDTO> deleteUtente(Long id);
}
