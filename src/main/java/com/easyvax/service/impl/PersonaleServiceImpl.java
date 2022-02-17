package com.easyvax.service.impl;


import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.repository.PersonaleRepository;
import com.easyvax.repository.ProvinciaRepository;
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
    private final ProvinciaRepository provinciaRepository;
    private static PersonaleEnum personaleEnum;

    @Override
    public PersonaleDTO insertpersonale(PersonaleDTO personale) {
        return null;
    }

    @Override
    public PersonaleDTO getDetails(Long id) {
        return null;
    }

    @Override
    public PersonaleDTO updateAnagrafica(PersonaleDTO personale) {
        return null;
    }

    @Override
    public List<PersonaleDTO> findAll() {
        if(!personaleRepository.findAll().isEmpty())
            return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else{
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_NE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> finByCap(String cap) {
        if(cap != null && (provinciaRepository.existsByCap(cap)))
            return personaleRepository.findByCap(cap).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_IDNE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> findByCognome(String cognome) {
        if(cognome != null && (personaleRepository.existsByCognome(cognome)))
            return personaleRepository.findByCognome(cognome).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_IDNE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }


    @Override
    public List<PersonaleDTO> findByRuolo(String ruolo) {
        if(ruolo != null && (personaleRepository.existsByRuolo(ruolo)))
            return personaleRepository.findByRuolo(ruolo).stream().map(PersonaleDTO::new).collect(Collectors.toList());
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_IDNE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public List<PersonaleDTO> deletePersonale(Long id) {

        if(personaleRepository.existsById(id)) {
            personaleRepository.deleteById(id);
            return personaleRepository.findAll().stream().map(PersonaleDTO::new).collect(Collectors.toList());
        }
        else {
            personaleEnum = PersonaleEnum.getPersonaleEnumByMessageCode("PERS_DLE");
            throw new ApiRequestException(personaleEnum.getMessage());
        }
    }

    @Override
    public PersonaleDTO updateRuolo(PersonaleDTO personale) {
        return null;
    }
}
