package com.easyvax.service.impl;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.VaccinoEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.VaccinoRepository;
import com.easyvax.service.service.CentroVaccinaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CentroVaccinaleServiceImpl implements CentroVaccinaleService {

    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private static CentroVaccinaleEnum centroVaccinaleEnum;

    @Override
    public CentroVaccinaleDTO findbyName(String nome) {

        if(nome != null && (centroVaccinaleRepository.existsByNome(nome)))
            return centroVaccinaleRepository.findByNome(nome);
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }

    }

    @Override
    public List<CentroVaccinaleDTO> findAll() {
        if(!centroVaccinaleRepository.findAll().isEmpty())
            return centroVaccinaleRepository.findAll().stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CVS_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public List<CentroVaccinaleDTO> findByCap(String cap) {

        if(cap != null && (centroVaccinaleRepository.existsByCap(cap)))
            return centroVaccinaleRepository.findByCap(cap);
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CVS_CNF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }

    }

    @Override
    public List<CentroVaccinaleDTO> findByVaccino(VaccinoDTO vaccino) {
        return null;
    }

    @Override
    public CentroVaccinaleDTO insertCentro(CentroVaccinaleDTO centro) {

return null;
    }

    @Override
    public CentroVaccinaleDTO updateCentro(CentroVaccinaleDTO centro) {
        return null;
    }

    @Override
    public List<CentroVaccinaleDTO> deleteCentro(Long id) {

        if(centroVaccinaleRepository.existsById(id)) {
            centroVaccinaleRepository.deleteById(id);
            return centroVaccinaleRepository.findAll().stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        }
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_DLE");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

}
