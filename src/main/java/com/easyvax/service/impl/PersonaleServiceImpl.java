package com.easyvax.service.impl;


import com.easyvax.dto.PersonaleDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.PersonaleRepository;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.service.PersonleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class PersonaleServiceImpl implements PersonleService {

    private final PersonaleRepository personaleRepository;
    private final UtenteRepository utenteRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private static PersonaleEnum personaleEnum;
    private static CentroVaccinaleEnum centroVaccinaleEnum;

    /**
     * Associo il personale ad una struttura
     *
     * @param personaleDTO
     * @return PersonaleDTO
     */
    @Override
    public PersonaleDTO insertpersonale(PersonaleDTO personaleDTO) {

        if (!personaleRepository.existsByUtente_Id(personaleDTO.getIdUtente())) {
            Personale personale = new Personale(personaleDTO);
            Utente utente = utenteRepository.findById(personaleDTO.getIdUtente()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(personaleDTO.getIdCentro()).get();
            personale.setUtente(utente);
            personale.setCentroVaccinale(cv);
            utente.setRuolo(RoleEnum.ROLE_PERSONALE);
            personaleRepository.save(personale);

            return new PersonaleDTO(personale);
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_AE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }

    }

    /**
     * Cerco tutto il personale
     *
     * @return List<PersonaleDTO>
     */

    @Override
    public List<PersonaleDTO> findAll() {
        if (!personaleRepository.findAll().isEmpty())
            return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    /**
     * Cerco il personale in base al centro vaccinale
     *
     * @param id
     * @return List<PersonaleDTO>
     */
    @Override
    public List<PersonaleDTO> findByCentroVaccinale(Long id) {

        if (id != null && centroVaccinaleRepository.existsById(id)) {
            return personaleRepository.findByCentroVaccinale_Id(id).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        } else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    /**
     * Cerco il personale in base al cognome
     *
     * @param cognome
     * @return List<PersonaleDTO>
     */
    @Override
    public List<PersonaleDTO> findByCognome(String cognome) {
        if (cognome != null && (utenteRepository.existsByCognome(cognome)))
            return personaleRepository.findByCognome(cognome).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NF");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    /**
     * Cerco il personale in base al codice fiscale
     *
     * @param cf
     * @return PersonaleDTO
     */
    @Override
    public PersonaleDTO findByCodFiscale(String cf) {
        if (cf != null && (utenteRepository.existsByCodFiscale(cf))) {
            return new PersonaleDTO(personaleRepository.findByCodFisc(cf));
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NF");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

/*
    @Override
    public List<PersonaleDTO> findByRuolo(String ruolo) {
        if(ruolo != null && (personaleRepository.existsByRuolo(ruolo)))
            return personaleRepository.findByRuolo(ruolo).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NF");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }*/

    /**
     * Elimino il personale se esiste, successivamente elimino e restituisco true. Altrimenti genero eccezione custom per il front-end
     * Resetto anche i privilegi a quello di USER fino a quando non riceve una nuova assegnazione da personale
     *
     * @param id
     * @return List<PersonaleDTO>
     */
    @Override
    public Boolean deletePersonale(Long id) {

        if (personaleRepository.existsById(id)) {

            //Per resettare il ruolo
           /*
            Personale personale = personaleRepository.findById(id).get();
            Utente utente = utenteRepository.findById(personale.getUtente().getId()).get();
            utente.setRuolo(RoleEnum.ROLE_USER);
            utenteRepository.save(utente);*/

            personaleRepository.deleteById(id);
            return true;
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_DLE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    /**
     * Eseguo l'update del personale
     *
     * @param personaleDTO
     * @return List<PersonaleDTO>
     */
    @Override
    public List<PersonaleDTO> updatePersonale(PersonaleDTO personaleDTO) {
        if (personaleRepository.existsById(personaleDTO.id)) {
            Utente utente = utenteRepository.findById(personaleDTO.getIdUtente()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(personaleDTO.getIdCentro()).get();

            if (utenteRepository.existsById(utente.getId()) && centroVaccinaleRepository.existsById(cv.getId())) {

                Personale personale = new Personale(personaleDTO);

                personale.setCentroVaccinale(cv);
                personale.setUtente(utente);

                personaleRepository.save(personale);

                return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
            } else {
                personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_UE");
                throw new ApiRequestException(personaleEnum.getMessage());
            }
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NF");
            throw new ApiRequestException(personaleEnum.getMessage());
        }

    }
}
