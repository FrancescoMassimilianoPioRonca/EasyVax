package com.easyvax.service.service;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.model.Personale;

import java.util.List;


public interface PersonleService {


    PersonaleDTO insertpersonale(PersonaleDTO personale);

    List<PersonaleDTO> findAll();

    List<PersonaleDTO> finByCentroVaccinale(Long id);

    List<PersonaleDTO> findByCognome(String cognome);

    PersonaleDTO findByCodFiscale(String cf);

    List<PersonaleDTO> findByRuolo(String ruolo);


    /**
     * Riservato agli admin
     * @param id
     * @return
     */
    List<PersonaleDTO> deletePersonale(Long id);
    List<PersonaleDTO> updatePersonale(PersonaleDTO personale);

}
