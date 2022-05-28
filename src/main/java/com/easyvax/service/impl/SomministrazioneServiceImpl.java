package com.easyvax.service.impl;


import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.exception.enums.SomministrazioneEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.service.SomministrazioneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.relational.core.sql.FalseCondition;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class SomministrazioneServiceImpl implements SomministrazioneService {

    private final SomministrazioneRepository somministrazioneRepository;
    private final VaccinoRepository vaccinoRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private final UtenteRepository utenteRepository;
    private static SomministrazioneEnum somministrazioneEnum;
    private JavaMailSender mailSender;

    /**
     * Inserisco una nuova somministrazione, controllando che la data sia valida quindi che non infranga il limite dei 2 giorni prima e che l'utente non abbia fatto
     * qualsiasi altra vaccinazione nell'arco dei 6 mesi precedenti. Inserendo una somministrazione viene inviata una email riepilogativa
     *
     * @param somministrazioneDTO
     * @return SomministrazioneDTO
     */
    @Override
    public SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazioneDTO) {


        if (somministrazioneRepository.findByUtente_IdAndVaccino_IdAndDataSomministrazione(somministrazioneDTO.getIdUtente(), somministrazioneDTO.getIdVaccino(), somministrazioneDTO.getData()) == 0) {

            Somministrazione somministrazione = new Somministrazione(somministrazioneDTO);
            Utente utente = utenteRepository.findById(somministrazioneDTO.getIdUtente()).get();

            if (somministrazioneRepository.checkVaccini(utente.getId(), somministrazioneDTO.data.minusMonths(6)) == 0) {
                Vaccino vaccino = vaccinoRepository.findById(somministrazioneDTO.getIdVaccino()).get();
                CentroVaccinale cv = centroVaccinaleRepository.findById(somministrazioneDTO.getIdCentro()).get();

                if (utenteRepository.existsById(utente.getId()) && centroVaccinaleRepository.existsById(cv.getId()) && vaccinoRepository.existsById(vaccino.getId()) ) {

                    String randomCode = RandomString.make(12);
                    if (!somministrazioneRepository.existsByCodiceSomm(randomCode))
                        somministrazione.setCodiceSomm(randomCode);
                    else {
                        somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_AE");
                        throw new ApiRequestException(somministrazioneEnum.getMessage());
                    }
                    LocalDate today = LocalDate.now();
                    LocalDate giornoSomm = somministrazione.getDataSomministrazione();

                    if (ChronoUnit.DAYS.between(today, giornoSomm) >= 2 && today.isBefore(somministrazione.getDataSomministrazione())) {

                        somministrazione.setDataSomministrazione(somministrazioneDTO.getData());
                        somministrazione.setOraSomministrazione(somministrazioneDTO.getOra());
                        somministrazione.setUtente(utente);
                        somministrazione.setCentro(cv);
                        somministrazione.setVaccino(vaccino);
                        somministrazione.setInAttesa(Boolean.FALSE);
                        somministrazioneRepository.save(somministrazione);
                    } else {
                        somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IE");
                        throw new ApiRequestException(somministrazioneEnum.getMessage());
                    }
                    return new SomministrazioneDTO(somministrazione);
                } else {
                    somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IE");
                    throw new ApiRequestException(somministrazioneEnum.getMessage());
                }
            } else {
                somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_UA");
                throw new ApiRequestException(somministrazioneEnum.getMessage());
            }

        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_AE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }


    /**
     * Invio email riepilogativa
     *
     * @param cod
     * @param toAddress
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
     public void sendEmail(String cod, String toAddress) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "I dettagli della tua prenotazione";
        String content = "Gentile utente,<br>"
                + "eccco il codice della tua prenotazione [[codice]]<br>"
                + "Con questo codice potrai scaricare la ricevuta cliccando sul seguente link<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">DETTAGLI</a></h3>"
                + " Saluti,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[codice]]", cod);

        helper.setText(content, true);

        mailSender.send(message);
    }


    /**
     * Modifico la prenotazione controllando i vincoli
     *
     * @param code
     * @param somministrazioneDTO
     * @return
     */
    @Override
    public SomministrazioneDTO updateSomministrazione(String code, SomministrazioneDTO somministrazioneDTO) {

        LocalDate today = LocalDate.now();
        LocalDate giornoSomm = somministrazioneDTO.getData();

        if (somministrazioneRepository.existsByCodiceSomm(code) && somministrazioneDTO.inAttesa != Boolean.TRUE && today.isBefore(somministrazioneDTO.getData())) {
            Somministrazione somministrazione = somministrazioneRepository.findByCodiceSomm(code);
            if (somministrazioneDTO.getData() != somministrazione.getDataSomministrazione() || somministrazioneDTO.getOra() != somministrazione.getOraSomministrazione()) {
                if (ChronoUnit.DAYS.between(today, giornoSomm) >= 2 && today.isBefore(somministrazione.getDataSomministrazione())) {
                    somministrazione.setDataSomministrazione(somministrazioneDTO.getData());
                    somministrazione.setOraSomministrazione(somministrazioneDTO.getOra());
                    somministrazioneRepository.save(somministrazione);
                    return new SomministrazioneDTO(somministrazione);
                } else {
                    somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_DE");
                    throw new ApiRequestException(somministrazioneEnum.getMessage());
                }
            } else {
                somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_DE");
                throw new ApiRequestException(somministrazioneEnum.getMessage());
            }
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_NF");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    /**
     * Ricevo i dettagli di una prenotazione
     *
     * @param id
     * @return SomministrazioneDTO
     */
    @Override
    public SomministrazioneDTO getDetails(Long id) {
        if (somministrazioneRepository.existsById(id)) {
            return new SomministrazioneDTO(somministrazioneRepository.getById(id));
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_NF");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    /**
     * Ricevo tutte le prenotazioni
     *
     * @return List<SomministrazioneDTO>
     */
    @Override
    public List<SomministrazioneDTO> findAll() {

        if (!somministrazioneRepository.findAll().isEmpty()) {
            return somministrazioneRepository.findAll().stream().map(SomministrazioneDTO::new).collect(Collectors.toList());
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    /**
     * Cerco le prenotazioni in base al codice fiscale
     *
     * @param cf
     * @return List<SomministrazioneDTO>
     */
    @Override
    public List<SomministrazioneDTO> findByUtente(String cf) {
        if (cf != null && utenteRepository.existsByCodFiscale(cf.toUpperCase(Locale.ROOT))) {
            return somministrazioneRepository.findbyUtente(cf.toUpperCase(Locale.ROOT)).stream().map(SomministrazioneDTO::new).collect(Collectors.toList());
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    /**
     * Cerco una prenotazione in base al codice
     *
     * @param cod
     * @return Somministrazione DTO
     */
    @Override
    public SomministrazioneDTO findByCod(String cod) {
        if (cod != null && somministrazioneRepository.existsByCodiceSomm(cod)) {
            return new SomministrazioneDTO(somministrazioneRepository.findByCodiceSomm(cod));
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_IDNE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }

    /**
     * Elimino una prenotazione
     *
     * @param id
     * @return List<SomministrazioneDTO>
     */
    @Override
    public boolean deletePrenotazione(Long id) {
        if (somministrazioneRepository.existsById(id)) {
            Somministrazione somministrazione = somministrazioneRepository.findById(id).get();
            if (somministrazione.getInAttesa() != Boolean.TRUE)
                somministrazioneRepository.deleteById(id);
            return true;
        } else {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_DLE");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }
    }


}
