package com.easyvax.service.impl;


import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.ProvinciaEnum;
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
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class PersonaleServiceImpl implements PersonleService {

    private final PersonaleRepository personaleRepository;
    private final UtenteRepository utenteRepository;
    private final ProvinciaRepository provinciaRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private static PersonaleEnum personaleEnum;
    private static CentroVaccinaleEnum centroVaccinaleEnum;

    @Override
    public PersonaleDTO insertpersonale(PersonaleDTO personaleDTO) {

        if (!personaleRepository.existsByUtente_Id(personaleDTO.getIdUtente())) {
            Personale personale = new Personale(personaleDTO);
            Utente utente = utenteRepository.findById(personaleDTO.getIdUtente()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(personaleDTO.getIdCentro()).get();
            personale.setUtente(utente);
            personale.setCentroVaccinale(cv);

            personale = personaleRepository.save(personale);

            return new PersonaleDTO(personale);
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_AE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }

    }


    @Override
    public List<PersonaleDTO> findAll() {
        if (!personaleRepository.findAll().isEmpty())
            return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> findByCentroVaccinale(Long id) {

        if (id != null && centroVaccinaleRepository.existsById(id)) {
            return personaleRepository.findByCentroVaccinale_Id(id).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        } else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> findByCognome(String cognome) {
        if (cognome != null && (utenteRepository.existsByCognome(cognome)))
            return personaleRepository.findByCognome(cognome).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NF");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

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

    @Override
    public List<PersonaleDTO> deletePersonale(Long id) {

        if (personaleRepository.existsById(id)) {
            personaleRepository.deleteById(id);
            return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
        } else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_DLE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> updatePersonale(PersonaleDTO personaleDTO) {
        if (personaleRepository.existsById(personaleDTO.id)) {
            Utente utente = utenteRepository.findById(personaleDTO.getIdUtente()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(personaleDTO.getIdCentro()).get();

            if (utenteRepository.existsById(utente.getId()) && centroVaccinaleRepository.existsById(cv.getId())) {

                Personale personale = new Personale(personaleDTO);

                personale.setCentroVaccinale(cv);
                personale.setUtente(utente);

                personale = personaleRepository.save(personale);

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
