package com.easyvax.service.impl;


import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.exception.enums.VaccinoEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.VaccinoRepository;
import com.easyvax.service.service.VaccinoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VaccinoServiceImpl implements VaccinoService {

    private final VaccinoRepository vaccinoRepository;
    private  VaccinoEnum vaccinoEnum;

    @Override
    public List<VaccinoDTO> findAll() {
        if(!vaccinoRepository.findAll().isEmpty())
            return vaccinoRepository.findAll().stream().map(VaccinoDTO::new).collect(Collectors.toList());
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VAXS_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }
    }

    @Override
    public List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica) {

        if(casaFarmaceutica != null && !(vaccinoRepository.findByCasaFarmaceutica(casaFarmaceutica).isEmpty()))
            return vaccinoRepository.findVaccinoByCasaFarmaceutica(casaFarmaceutica);
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_CASA_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }
    }

    @Override
    public VaccinoDTO insertVaccino(VaccinoDTO vaccinoDTO) {

        Vaccino vaccino = new Vaccino(vaccinoDTO);
        vaccino = vaccinoRepository.save(vaccino);

        return new VaccinoDTO(vaccino);
    }

    @Override
    public List<VaccinoDTO> deleteVaccino(Long id) {

        if(vaccinoRepository.existsById(id)) {
            vaccinoRepository.deleteById(id);
            return vaccinoRepository.findAll().stream().map(VaccinoDTO::new).collect(Collectors.toList());
        }
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_DLE");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }

    }

    @Override
    public List<VaccinoDTO> updateVaccino(VaccinoDTO vaccinoDTO) {

        if (vaccinoDTO.id != null && vaccinoRepository.existsById(vaccinoDTO.id)) {
            Vaccino vaccino = vaccinoRepository.findById(vaccinoDTO.getId()).get();
            vaccino.setNome(vaccinoDTO.nome);
            vaccino.setDataApprovazioneVaccino(vaccinoDTO.getDataApprovazioneVaccino());
            vaccino.setCasaFarmaceutica(vaccinoDTO.casaFarmaceutica);
            vaccinoRepository.save(vaccino);
            return (List<VaccinoDTO>) new VaccinoDTO(vaccino);
        }

        throw new RuntimeException("Errore, impossibile aggiornare le impostazioni utente");
    }



}
