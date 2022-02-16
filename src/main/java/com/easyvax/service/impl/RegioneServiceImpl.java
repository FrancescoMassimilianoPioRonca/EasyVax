package com.easyvax.service.impl;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.RegioneDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.RegioneEnum;
import com.easyvax.exception.enums.VaccinoEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.Regione;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.service.RegioneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class RegioneServiceImpl implements RegioneService {

    private final RegioneRepository regioneRepository;
    private final ProvinciaRepository provinciaRepository;
    private static RegioneEnum regioneEnum;

    @Override
    public List<RegioneDTO> findAll() {
        if(!regioneRepository.findAll().isEmpty())
            return regioneRepository.findAll().stream().map(RegioneDTO::new).collect(Collectors.toList());
        else {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("RI_NE");
            throw new ApiRequestException(regioneEnum.getMessage());
        }
    }

    @Override
    public RegioneDTO findByNome(String nome) {

        if(nome != null && (regioneRepository.existsByNome(nome)))
            return new RegioneDTO(regioneRepository.findByNome(nome));
        else {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_NF");
            throw new ApiRequestException(regioneEnum.getMessage());
        }

    }

    @Override
    public RegioneDTO findByProvincia(String provincia) {

        if(provincia != null && (provinciaRepository.existsByNome(provincia)))
            return new RegioneDTO(regioneRepository.findByProvincia(provincia));
        else {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_NF");
            throw new ApiRequestException(regioneEnum.getMessage());
        }
    }

    @Override
    public RegioneDTO insertRegione(RegioneDTO regioneDTO) {

         Regione regione = new Regione(regioneDTO);

        if(regioneRepository.existsByNome(regioneDTO.nome)) {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_AE");
            throw new ApiRequestException(regioneEnum.getMessage());
        }else if (regioneDTO.nome == null) {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_EF");
            throw new ApiRequestException(regioneEnum.getMessage());
        }
        else {
            regione = regioneRepository.save(regione);
            return new RegioneDTO(regione);
        }
    }

    @Override
    public List<RegioneDTO> updateRegione(RegioneDTO regioneDTO) {
        if (regioneRepository.existsById(regioneDTO.id)) {
            Regione regione = regioneRepository.findById(regioneDTO.getId()).get();

            if(!regioneRepository.existsByNome(regioneDTO.nome)) {

                regione.setNome(regioneDTO.nome);


                regioneRepository.save(regione);
            }

            else{
                regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_AE");
                throw new ApiRequestException(regioneEnum.getMessage());
            }
        }
        else
        {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_NF");
            throw new ApiRequestException(regioneEnum.getMessage());
        }

        return regioneRepository.findAll().stream().map(RegioneDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<RegioneDTO> deleteRegione(Long id) {
        if(regioneRepository.existsById(id)) {
            regioneRepository.deleteById(id);
            return regioneRepository.findAll().stream().map(RegioneDTO::new).collect(Collectors.toList());
        }
        else {
            regioneEnum = RegioneEnum.getRegioneEnumByMessageCode("R_NF");
            throw new ApiRequestException(regioneEnum.getMessage());
        }
    }
}
