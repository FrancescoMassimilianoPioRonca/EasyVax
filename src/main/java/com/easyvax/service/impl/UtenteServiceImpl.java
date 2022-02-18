package com.easyvax.service.impl;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.DTO.UtenteDTO;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.ProvinciaEnum;
import com.easyvax.exception.enums.UtenteEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Utente;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.service.UtenteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor

public class UtenteServiceImpl  implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final ProvinciaRepository provinciaRepository;
    private static UtenteEnum utenteEnum;

    @Override
    public UtenteDTO insertUtente(UtenteDTO utenteDTO) {

        if(!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascitaAndRuolo(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita(), utenteDTO.getRuolo())){
            Provincia provincia = provinciaRepository.findById(utenteDTO.getResidenza()).get();
            Utente utente = utenteRepository.findById(utenteDTO.id).get();
            utente.setNome(utenteDTO.nome);
            utente.setCognome(utenteDTO.cognome);
            utente.setCodFiscale(utenteDTO.getCodFiscale());
            utente.setDataNascita(utenteDTO.getDataNascita());
            utente.setPassword(utenteDTO.getPassword());
            utente.setRuolo(utente.getRuolo());
            utente.setProvincia(provincia);


            utente = utenteRepository.save(utente);

            return new UtenteDTO(utente);
        }
        else{
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    @Override
    public UtenteDTO getDetails(Long id) {
        if(id!=null && utenteRepository.existsById(id)){
            return new UtenteDTO(utenteRepository.findById(id).get());
        }
        else{
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    @Override
    public List<UtenteDTO> updateAnagrafica(UtenteDTO utenteDTO) {

        if (utenteRepository.existsById(utenteDTO.id)) {
            Provincia provincia = provinciaRepository.findById(utenteDTO.getResidenza()).get();

            if(!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascitaAndRuolo(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita(),utenteDTO.getRuolo()) && provinciaRepository.existsById(utenteDTO.getResidenza())) {

                Utente utente = new Utente(utenteDTO);

                utente.setNome(utenteDTO.getNome());
                utente.setCodFiscale(utenteDTO.getCognome());
                utente.setDataNascita(utenteDTO.getDataNascita());
                utente.setPassword(utenteDTO.getPassword());
                utente.setCognome(utenteDTO.getCognome());
                utente.setProvincia(provincia);
                if(utenteDTO.getRuolo().equals("AMMINISTRATORE")){
                    utente.setRuolo(utenteDTO.getRuolo());
                }
            }
            else{
                utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
                throw new ApiRequestException(utenteEnum.getMessage());
            }
        }
        else
        {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }

        return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<UtenteDTO> findAll() {
        if(!utenteRepository.findAll().isEmpty())
            return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
        else{
             utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTI_NE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

   /* @Override
    public List<UtenteDTO> finByCap(String cap) {
        if(cap != null && (provinciaRepository.existsByCap(cap)))
            return utenteRepository.findByCap(cap).stream().map(UtenteDTO::new).collect(Collectors.toList());
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_CAP_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    @Override
    public List<UtenteDTO> findByCognome(String cognome) {
        if(cognome != null && (utenteRepository.existsByCognome(cognome)))
            return utenteRepository.findByCognome(cognome).stream().map(UtenteDTO::new).collect(Collectors.toList());
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }
*/
    @Override
    public List<UtenteDTO> deleteUtente(Long id) {
        if(utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
        }
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_DLE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    /*@Override
    public List<UtenteDTO> findByRuolo(Boolean personale) {
        return null;
    }*/
}
