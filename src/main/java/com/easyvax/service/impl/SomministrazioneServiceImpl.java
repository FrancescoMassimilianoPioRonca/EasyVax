package com.easyvax.service.impl;


import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.SomministrazioneDTO;
import com.easyvax.DTO.UtenteDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.SomministrazioneEnum;
import com.easyvax.exception.enums.UtenteEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.service.SomministrazioneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class SomministrazioneServiceImpl implements SomministrazioneService {

    private final SomministrazioneRepository somministrazioneRepository;
    private final PersonaleRepository personaleRepository;
    private final VaccinoRepository vaccinoRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private final UtenteRepository utenteRepository;
    private static SomministrazioneEnum somministrazioneEnum;
    @Override
    public SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazioneDTO) {
        if(somministrazioneRepository.findByUtente_IdAndVaccino_IdAndDataSomministrazione(somministrazioneDTO.getIdUtente(),somministrazioneDTO.getIdVaccino(),somministrazioneDTO.getData()).isEmpty()) {

            Somministrazione somministrazione = new Somministrazione(somministrazioneDTO);

            Utente utente = utenteRepository.findById(somministrazioneDTO.getIdUtente()).get();
            Vaccino vaccino = vaccinoRepository.findById(somministrazioneDTO.getIdVaccino()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(somministrazioneDTO.getIdCentro()).get();

            if (utenteRepository.existsById(utente.getId()) && centroVaccinaleRepository.existsById(cv.getId()) && vaccinoRepository.existsById(vaccino.getId())) {
                somministrazione.setCodiceSomm(somministrazioneDTO.codiceSomm);
                somministrazione.setDataSomministrazione(somministrazioneDTO.getData());
                somministrazione.setOraSomministrazione(somministrazioneDTO.getOra());
                somministrazione.setUtente(utente);
                somministrazione.setCentro(cv);
                somministrazione.setVaccino(vaccino);

                somministrazione = somministrazioneRepository.save(somministrazione);

                return new SomministrazioneDTO(somministrazione);
            } else {
                somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IE");
                throw new ApiRequestException(somministrazioneEnum.getMessage());
            }
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_AE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public SomministrazioneDTO updateSomministrazione(SomministrazioneDTO somministrazioneDTO) {

        if(somministrazioneRepository.existsByCodiceSomm(somministrazioneDTO.codiceSomm)){
            Somministrazione somministrazione = new Somministrazione(somministrazioneDTO);

            somministrazione.setDataSomministrazione(somministrazioneDTO.getData());
            somministrazione.setOraSomministrazione(somministrazioneDTO.getOra());

            somministrazione= somministrazioneRepository.save(somministrazione);

            return new SomministrazioneDTO(somministrazione);
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_NF");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public SomministrazioneDTO getDetails(Long id) {
        if(somministrazioneRepository.existsById(id)) {
            return new SomministrazioneDTO(somministrazioneRepository.getById(id));
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_NF");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public List<SomministrazioneDTO> findAll() {

        if(!somministrazioneRepository.findAll().isEmpty()){
            return somministrazioneRepository.findAll().stream().map(SomministrazioneDTO::new).collect(Collectors.toList());
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public List<SomministrazioneDTO> findByUtente(String cf) {
        if(cf!=null && utenteRepository.existsByCodFiscale(cf)){
            return somministrazioneRepository.findbyUtente(cf);
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public SomministrazioneDTO findByCod(String cod) {
        if(cod!=null && somministrazioneRepository.existsByCodiceSomm(cod)){
            return new SomministrazioneDTO(somministrazioneRepository.findByCodiceSomm(cod));
        }
        else{
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    @Override
    public List<SomministrazioneDTO> deletePrenotazione(Long id) {
        if(somministrazioneRepository.existsById(id)) {
            somministrazioneRepository.deleteById(id);
            return somministrazioneRepository.findAll().stream().map(SomministrazioneDTO::new).collect(Collectors.toList());
        }
        else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_DLE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }


}
