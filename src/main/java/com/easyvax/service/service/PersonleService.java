package com.easyvax.service.service;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.model.Personale;

import java.util.List;


public interface PersonleService {


    PersonaleDTO insertpersonale(PersonaleDTO personale);

    PersonaleDTO getDetails(Long id);

    PersonaleDTO updateAnagrafica(PersonaleDTO personale);

    List<PersonaleDTO> findAll();

    List<PersonaleDTO> finByCap(String cap);

    List<PersonaleDTO> findByCognome(String cognome);


    List<PersonaleDTO> findByRuolo(String ruolo);


    /**
     * Riservato agli admin
     * @param id
     * @return
     */
    List<PersonaleDTO> deletePersonale(Long id);
    PersonaleDTO updateRuolo(PersonaleDTO personale);

}
