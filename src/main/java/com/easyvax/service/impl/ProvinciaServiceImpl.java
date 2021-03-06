package com.easyvax.service.impl;

import com.easyvax.dto.ProvinciaDTO;
import com.easyvax.exception.enums.ProvinciaEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.Provincia;
import com.easyvax.model.Regione;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.service.ProvinciaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor

public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;
    private final RegioneRepository regioneRepository;
    private static ProvinciaEnum provinciaEnum;

    /**
     * Cerco tutte le provincie
     *
     * @return List<ProvinciaDTO>
     */
    @Override
    public List<ProvinciaDTO> findAll() {
        if (!provinciaRepository.findAll().isEmpty())
            return provinciaRepository.findAll().stream().map(ProvinciaDTO::new).collect(Collectors.toList());
        else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("PE_NE");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }
    }

    /**
     * Cerco le provincie in base al nome
     *
     * @param nome
     * @return List<ProvinciaDTO>
     */
    @Override
    public List<ProvinciaDTO> findByNome(String nome) {
        if (nome != null && (provinciaRepository.existsByNome(nome))) {
            return provinciaRepository.findByNome(nome).stream().map(ProvinciaDTO::new).collect(Collectors.toList());
        } else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_NF");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }
    }

    /**
     * Cerco le provincie in base alla regione
     *
     * @param regione
     * @return List<ProvinciaDTO>
     */
    @Override
    public List<ProvinciaDTO> findByRegione(String regione) {

        if (regione != null && (regioneRepository.existsByNome(regione))) {
            Regione reg = regioneRepository.findByNome(regione);
            return provinciaRepository.findByRegione_Id(reg.getId()).stream().map(ProvinciaDTO::new).collect(Collectors.toList());
        } else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("PR_NE");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }
    }

    /**
     * Cerco le provincie in base al cap
     *
     * @param cap
     * @return ProvinciaDTO
     */
    @Override
    public ProvinciaDTO findByCap(String cap) {
        if (cap != null && (provinciaRepository.existsByCap(cap)))
            return new ProvinciaDTO(provinciaRepository.findByCap(cap));
        else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("C_NF");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }
    }

    /**
     * Inserisco una nuova provincia con i dovuti controlli
     *
     * @param provinciaDTO
     * @return ProviciaDTO
     */
    @Override
    public ProvinciaDTO insertProvincia(ProvinciaDTO provinciaDTO) {

        if (provinciaRepository.existsByNomeAndRegione_IdAndCap(provinciaDTO.nome, provinciaDTO.idRegione, provinciaDTO.cap)) {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_AE");
            throw new ApiRequestException(provinciaEnum.getMessage());
        } else if (provinciaDTO.nome == null) {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_EF");
            throw new ApiRequestException(provinciaEnum.getMessage());
        } else if (provinciaRepository.existsByNomeAndCap(provinciaDTO.nome, provinciaDTO.cap)) {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_AE");
            throw new ApiRequestException(provinciaEnum.getMessage());
        } else {
            Provincia provincia = new Provincia(provinciaDTO);
            Regione regione = regioneRepository.findById(provinciaDTO.idRegione).get();
            provincia.setRegione(regione);
            provinciaRepository.save(provincia);
            return new ProvinciaDTO(provincia);

        }
    }

    /**
     * Modifico una provincia esistente
     *
     * @param provinciaDTO
     * @return List<ProvinciaDTO>
     */
    @Override
    public List<ProvinciaDTO> updateProvincia(ProvinciaDTO provinciaDTO) {

        if (provinciaRepository.existsById(provinciaDTO.id)) {
            Provincia provincia = provinciaRepository.findById(provinciaDTO.getId()).get();
            Regione regione = regioneRepository.findById(provinciaDTO.getIdRegione()).get();

            if (!provinciaRepository.existsByNomeAndRegione_IdAndCap(provinciaDTO.nome, provinciaDTO.getIdRegione(), provinciaDTO.cap) && !provinciaRepository.existsByCap(provinciaDTO.cap)) {

                provincia.setNome(provinciaDTO.nome);
                provincia.setCap(provinciaDTO.cap);
                provincia.setRegione(regione);

                provinciaRepository.save(provincia);
            } else {
                provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_AE");
                throw new ApiRequestException(provinciaEnum.getMessage());
            }
        } else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_NF");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }

        return provinciaRepository.findAll().stream().map(ProvinciaDTO::new).collect(Collectors.toList());
    }

    /**
     * Elimino una provincia
     *
     * @param id
     * @return List<ProvinciaDTO>
     */
    @Override
    public boolean deleteProvincia(Long id) {

        if (provinciaRepository.existsById(id)) {
            provinciaRepository.deleteById(id);
            return true;
        } else {
            provinciaEnum = ProvinciaEnum.getProvinciaByMessageCode("P_NF");
            throw new ApiRequestException(provinciaEnum.getMessage());
        }
    }
}
