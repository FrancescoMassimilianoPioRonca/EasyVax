package com.easyvax.service.impl;

import com.easyvax.DTO.CentroVaccinaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.VaccinoEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
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
    private final VaccinoRepository vaccinoRepository;
    private final RegioneRepository regioneRepository;
    private final ProvinciaRepository provinciaRepository;
    private static CentroVaccinaleEnum centroVaccinaleEnum;

    @Override
    public List<CentroVaccinaleDTO> findbyName(String nome) {

        if(nome != null && (centroVaccinaleRepository.existsByNome(nome)))
            return centroVaccinaleRepository.findByNome(nome).stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
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

        if(cap != null && (provinciaRepository.existsByCap(cap)))
            return centroVaccinaleRepository.findByCap(cap).stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CVS_CNF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }

    }

    @Override
    public List<CentroVaccinaleDTO> findByVaccino(Long id) {

        if(vaccinoRepository.existsById(id))
           return centroVaccinaleRepository.findByVaccino(id).stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CVV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public List<CentroVaccinaleDTO> findByProvincia(Long id) {

        if(provinciaRepository.existsById(id)){
            return centroVaccinaleRepository.findByProvincia_Id(id).stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        }else
        {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_IDNE");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public List<CentroVaccinaleDTO> findByRegione(String regione) {

        if(regioneRepository.existsByNome(regione)){
            return centroVaccinaleRepository.findByRegione(regione).stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
        }else
        {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_RNE");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public CentroVaccinaleDTO insertCentro(CentroVaccinaleDTO centro) {

        CentroVaccinale centroVaccinale = new CentroVaccinale(centro);
        Provincia provincia = provinciaRepository.findById(centro.idProvincia).get();

        if(!centroVaccinaleRepository.existsByNomeAndProvincia_Id(centro.nome, provincia.getId())){
            if(centro.nome != null && centro.indirizzo != null && provinciaRepository.existsById(provincia.getId())) {
                centroVaccinale.setProvincia(provincia);
                centroVaccinale = centroVaccinaleRepository.save(centroVaccinale);
                return new CentroVaccinaleDTO(centroVaccinale);
            }
            else{
                centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_EF");
                throw new ApiRequestException(centroVaccinaleEnum.getMessage());
            }
        }
        else{
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CVP_AE");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    @Override
    public List<CentroVaccinaleDTO> updateCentro(CentroVaccinaleDTO centroDTO) {

        if (centroVaccinaleRepository.existsById(centroDTO.id) && provinciaRepository.existsById(centroDTO.idProvincia)) {
            CentroVaccinale centroVaccinale = centroVaccinaleRepository.findById(centroDTO.id).get();
            Provincia provincia = provinciaRepository.findById(centroDTO.idProvincia).get();

            if(!centroVaccinaleRepository.existsByNomeAndIndirizzoAndProvincia_Id(centroDTO.nome,centroDTO.indirizzo,centroDTO.idProvincia)) {

                centroVaccinale.setNome(centroDTO.nome);
                centroVaccinale.setIndirizzo(centroDTO.indirizzo);
                centroVaccinale.setProvincia(provincia);
                centroVaccinaleRepository.save(centroVaccinale);

            }

            else{
                centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NU");
                throw new ApiRequestException(centroVaccinaleEnum.getMessage());
            }
        }
        else
        {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }

        return centroVaccinaleRepository.findAll().stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());
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
