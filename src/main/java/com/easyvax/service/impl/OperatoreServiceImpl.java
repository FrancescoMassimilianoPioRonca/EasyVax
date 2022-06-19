package com.easyvax.service.impl;

import com.easyvax.dto.OperatoreDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.OperatoreEnum;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Operatore;
import com.easyvax.model.Utente;
import com.easyvax.repository.*;
import com.easyvax.service.service.OperatoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class OperatoreServiceImpl implements OperatoreService {


    private final OperatoreRepository operatoreRepository;
    private final UtenteRepository utenteRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private static OperatoreEnum operatoreEnum;
    private static CentroVaccinaleEnum centroVaccinaleEnum;

    /**
     * Associo un nuovo operatore presso la struttura
     *
     * @param operatoreDTO
     * @return OperatoreDTO
     */
    @Override
    public OperatoreDTO insertOperatore(OperatoreDTO operatoreDTO) {

        if (!operatoreRepository.existsByUtente_Id(operatoreDTO.getIdUtente())) {
            Operatore operatore = new Operatore(operatoreDTO);
            Utente utente = utenteRepository.findById(operatoreDTO.getIdUtente()).get();
            CentroVaccinale cv = centroVaccinaleRepository.findById(operatoreDTO.getIdCentro()).get();
            operatore.setUtente(utente);
            operatore.setCentroVaccinale(cv);
            utente.setRuolo(RoleEnum.ROLE_OPERATOR);
            operatoreRepository.save(operatore);

            return new OperatoreDTO(operatore);
        } else {
            operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_AE");
            throw new ApiRequestException(operatoreEnum.getMessage());
        }

    }

    /**
     * Cerco tutti gli operatori
     *
     * @return List<OperatoreDTO>
     */
    @Override
    public List<OperatoreDTO> findAll() {
        if (!operatoreRepository.findAll().isEmpty())
            return operatoreRepository.findAll().stream().map(OperatoreDTO::new).collect(Collectors.toList());
        else {
            operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_NE");
            throw new ApiRequestException(operatoreEnum.getMessage());
        }
    }

    /**
     * Cerco gli operatoi in base al centro vaccinale
     *
     * @param id
     * @return List<OperatoreDTO>
     */
    @Override
    public List<OperatoreDTO> findByCentroVaccinale(Long id) {

        if (id != null && centroVaccinaleRepository.existsById(id)) {
            return operatoreRepository.findByCentroVaccinale_Id(id).stream().map(OperatoreDTO::new).collect(Collectors.toList());
        } else {
            centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
            throw new ApiRequestException(centroVaccinaleEnum.getMessage());
        }
    }

    /**
     * Cerco l'operatore in base al codice fiscale
     *
     * @param cf
     * @return OperatoreDTO
     */
    @Override
    public OperatoreDTO findByCodFiscale(String cf) {
        if (cf != null && (utenteRepository.existsByCodFiscale(cf))) {
            return new OperatoreDTO(operatoreRepository.findByCodFisc(cf));
        } else {
            operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_NF");
            throw new ApiRequestException(operatoreEnum.getMessage());
        }
    }


    /**
     * Controllo se l'operatorre esiste, successivamente elimino e restituisco true. Altrimenti genero eccezione custom per il front-end
     * Resetto anche i privilegi a quello di USER fino a quando non riceve una nuova assegnazione da operatore
     *
     * @param id
     * @return List<OperatoreDTO>
     */
    @Override
    public boolean deleteOperatore(Long id) {

        if (operatoreRepository.existsById(id)) {

            //Per resettare il ruolo
           /*
            Operatore operatore = operatoreReository.findById(id).get();
            Utente utente = utenteRepository.findById(operatore.getUtente().getId()).get();
            utente.setRuolo(RoleEnum.ROLE_USER);
            utenteRepository.save(utente);*/

            operatoreRepository.deleteById(id);
            return true;
        } else {
            operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_DLE");
            throw new ApiRequestException(operatoreEnum.getMessage());
        }
    }

    /**
     * Modifico un operatore
     *
     * @param operatoreDTO
     * @return List<OperatoreDTO>
     */
    @Override
    public List<OperatoreDTO> updateOperatore(OperatoreDTO operatoreDTO) {
        if (operatoreRepository.existsById(operatoreDTO.id)) {
            if (centroVaccinaleRepository.existsById(operatoreDTO.getIdCentro()) && utenteRepository.existsById(operatoreDTO.getIdUtente())) {
                CentroVaccinale cv = centroVaccinaleRepository.findById(operatoreDTO.getIdCentro()).get();
                Utente utente = utenteRepository.findById(operatoreDTO.getIdUtente()).get();

                Operatore operatore = new Operatore(operatoreDTO);

                operatore.setCentroVaccinale(cv);
                operatore.setUtente(utente);

                operatoreRepository.save(operatore);

                return operatoreRepository.findAll().stream().map(OperatoreDTO::new).collect(Collectors.toList());
            } else {
                operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_UE");
                throw new ApiRequestException(operatoreEnum.getMessage());
            }
        } else {
            operatoreEnum = OperatoreEnum.getOperatoreEnumByMessageCode("OP_NF");
            throw new ApiRequestException(operatoreEnum.getMessage());
        }

    }
}
