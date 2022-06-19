package com.easyvax.service.impl;


import com.easyvax.dto.VaccinoDTO;
import com.easyvax.exception.enums.VaccinoEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.VaccinoRepository;
import com.easyvax.service.service.VaccinoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VaccinoServiceImpl implements VaccinoService {

    private final VaccinoRepository vaccinoRepository;
    private static VaccinoEnum vaccinoEnum;

    /**
     * Cerco tutti i vaccini
     *
     * @return List<VaccinoDTO>
     */
    @Override
    public List<VaccinoDTO> findAll() {
        if (!vaccinoRepository.findAll().isEmpty())
            return vaccinoRepository.findAll().stream().map(VaccinoDTO::new).collect(Collectors.toList());
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VAXS_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }
    }

    /**
     * Cerco un vaccino in base alla casa farmaceutica
     *
     * @param casaFarmaceutica
     * @return List<VaccinoDTO>
     */
    @Override
    public List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica) {

        if (casaFarmaceutica != null && !(vaccinoRepository.findByCasaFarmaceutica(casaFarmaceutica).isEmpty()))
            return vaccinoRepository.findVaccinoByCasaFarmaceutica(casaFarmaceutica).stream().map(VaccinoDTO::new).collect(Collectors.toList());
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_CASA_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }
    }

    /**
     * Cerco un vaccino in base al nome
     *
     * @param nome
     * @return VaccinoDTO
     */
    @Override
    public VaccinoDTO findByNome(String nome) {

        if (nome != null && (vaccinoRepository.existsByNome(nome)))
            return new VaccinoDTO(vaccinoRepository.findByNome(nome));
        else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }

    }

    /**
     * Inserisco un nuovo vaccino
     *
     * @param vaccinoDTO
     * @return VaccinoDTO
     */
    @Override
    public VaccinoDTO insertVaccino(VaccinoDTO vaccinoDTO) {

        Vaccino vaccino = new Vaccino();

        if (vaccinoRepository.existsByNomeAndCasaFarmaceutica(vaccinoDTO.nome, vaccinoDTO.casaFarmaceutica)) {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_AE");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        } else if (vaccinoDTO.nome == null || vaccinoDTO.casaFarmaceutica == null) {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VCC_FI");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        } else {

            vaccino.setNome(vaccinoDTO.nome);
            vaccino.setCasaFarmaceutica(vaccinoDTO.casaFarmaceutica);
            vaccinoRepository.save(vaccino);
            return new VaccinoDTO(vaccino);
        }
    }

    /**
     * Elimino il vaccino
     *
     * @param id
     * @return List<VaccinoDTO>
     */
    @Override
    public boolean deleteVaccino(Long id) {

        if (vaccinoRepository.existsById(id)) {
            vaccinoRepository.deleteById(id);
            return true;
        } else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_DLE");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }

    }

    /**
     * Modifico un vaccino
     *
     * @param vaccinoDTO
     * @return List<VaccinoDTO>
     */
    @Override
    public List<VaccinoDTO> updateVaccino(VaccinoDTO vaccinoDTO) {

        if (vaccinoRepository.existsById(vaccinoDTO.id)) {
            Vaccino vaccino = vaccinoRepository.findById(vaccinoDTO.getId()).get();

            if (!vaccinoRepository.existsByNomeAndCasaFarmaceutica(vaccinoDTO.nome, vaccinoDTO.casaFarmaceutica)) {

                vaccino.setNome(vaccinoDTO.nome);
                vaccino.setCasaFarmaceutica(vaccinoDTO.casaFarmaceutica);

                vaccinoRepository.save(vaccino);
            } else {
                vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VCC_NU");
                throw new ApiRequestException(vaccinoEnum.getMessage());
            }
        } else {
            vaccinoEnum = VaccinoEnum.getVaccinoEnumByMessageCode("VACC_NF");
            throw new ApiRequestException(vaccinoEnum.getMessage());
        }

        return vaccinoRepository.findAll().stream().map(VaccinoDTO::new).collect(Collectors.toList());
    }


}
