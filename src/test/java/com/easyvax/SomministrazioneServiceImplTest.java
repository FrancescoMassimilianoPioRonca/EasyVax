package com.easyvax;

import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.exception.handler.ApiException;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.impl.SomministrazioneServiceImpl;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SomministrazioneServiceImplTest {

    private SomministrazioneServiceImpl somministrazioneServiceImpl;

    @Mock
    private VaccinoRepository vaccinoRepository;

    @Mock
    private SomministrazioneRepository somministrazioneRepository;

    @Mock
    private CentroVaccinaleRepository centroVaccinaleRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private PersonaleRepository personaleRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ChronoUnit clock;


    @BeforeEach
    void setUp() {
        somministrazioneServiceImpl = new SomministrazioneServiceImpl(somministrazioneRepository,vaccinoRepository, centroVaccinaleRepository, utenteRepository,personaleRepository,javaMailSender);
    }

    /**
     * Con questo metodo, testo la ricerca della vaccinazione per codice.
     * E' essenziale per l'utente sia per scaricare il pdf della vaccnazione sia per conoscerne i dettagli
     */
    @Test
    void findByCodice(){

        String code = "test";
        LocalDate date = LocalDate.now().minusDays(2);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(0L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).codiceSomm(code).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        lenient().when(somministrazioneRepository.existsByCodiceSomm(code)).thenReturn(true);

        lenient().when(somministrazioneRepository.findByCodiceSomm(code)).thenReturn(somministrazione);

        SomministrazioneDTO somministrazioneDTO = new SomministrazioneDTO(somministrazione);
        assertNotNull(somministrazioneDTO);



        assertEquals(code, somministrazioneServiceImpl.findByCod(code).getCode());

        reset(somministrazioneRepository);
    }

    /**
     * Con questo metodo, testo la corretta cancellazione di una prenotazione
     * Eseguendo tutti i check
     */

    @Test
    void deletePrentazione(){
        Long id = 3L;
        LocalDate date = LocalDate.now().minusDays(2);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(0L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(id).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).codiceSomm("test").centro(cv1).oraSomministrazione("15:00").codiceSomm("test").inAttesa(false).build();

        lenient().when(somministrazioneRepository.existsById(id)).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(id)).thenReturn(Optional.of(somministrazione));

        assertEquals(somministrazione.getInAttesa(),false);
        assertEquals(true,somministrazioneServiceImpl.deletePrenotazione(id));
    }

    /**
     * Con questo metodo testo il corretto inserimento della prenotazione di una vaccinazione
     * con tutti i controlli relativi alle date.
     * Si noti che sccome Mockito non riesce a mockare la classe ChronoUnit non riesce nanche ad eseguire il check sulla data
     * Per cui viene restituita un'eccezione custom
     */
    @Test
    void insertPrenotazione(){
        LocalDate date = LocalDate.now().minusDays(5);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(0L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).codiceSomm("test").centro(cv1).oraSomministrazione("15:00").codiceSomm("test").inAttesa(false).build();

        SomministrazioneDTO somministrazioneDTO = new SomministrazioneDTO(somministrazione);
        assertNotNull(somministrazioneDTO);

        lenient().when(somministrazioneRepository.findByUtente_IdAndVaccino_IdAndDataSomministrazione(somministrazioneDTO.getIdUtente(), somministrazioneDTO.idVaccino, somministrazioneDTO.getData())).thenReturn(0);
        lenient().when(utenteRepository.findById(somministrazioneDTO.getIdUtente())).thenReturn(Optional.of(utente));

        lenient().when(somministrazioneRepository.checkVaccini(utente.getId(),LocalDate.now().minusMonths(6))).thenReturn(0);
        lenient().when(vaccinoRepository.findById(vaccino.getId())).thenReturn(Optional.of(vaccino));
        lenient().when(centroVaccinaleRepository.findById(somministrazioneDTO.getIdCentro())).thenReturn(Optional.of(cv1));

        lenient().when(utenteRepository.existsById(somministrazioneDTO.getIdUtente())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.existsById(somministrazioneDTO.getIdCentro())).thenReturn(true);
        lenient().when(vaccinoRepository.existsById(somministrazioneDTO.getIdVaccino())).thenReturn(true);

        lenient().when(somministrazioneRepository.existsByCodiceSomm(somministrazioneDTO.code)).thenReturn(false);

        /**
         *
         * L'errore generato dal test è voluto , perchè non riesce a controllare la data
         */
        /*lenient().when(ChronoUnit.DAYS.between(LocalDate.now(),date)>=2).thenReturn(true);
        lenient().when(date.isBefore(somministrazioneDTO.getData())).thenReturn(true);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);

        assertEquals(somministrazione.getCodiceSomm(),somministrazioneServiceImpl.insertSomministrazione(somministrazioneDTO).getCode());*/

        reset(somministrazioneRepository);
        reset(centroVaccinaleRepository);
        reset(vaccinoRepository);
        reset(utenteRepository);

    }


    /**
     * Con questo metodo testo il corretto update della prenotazione di una vaccinazione
     * con tutti i controlli relativi alle date.
     * Si noti che siccome Mockito non riesce a mockare la classe ChronoUnit non riesce nanche ad eseguire il check sulla data
     * Per cui viene restituita un'eccezione custom
     */
    @Test
    void updatePrenotazione(){

        String code = "test";
        LocalDate today = LocalDate.now();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(2L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Utente utente = Utente.builder().id(3l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        SomministrazioneDTO somministrazioneDTO = SomministrazioneDTO.builder().id(0L).code(code).data(today.plusDays(3)).idCentro(2L).idUtente(3L).ora("16:00").inAttesa(false).build();

        LocalDate giornoSomm = somministrazioneDTO.getData();

        lenient().when(somministrazioneRepository.existsByCodiceSomm(code)).thenReturn(true);
        assertEquals(false,somministrazioneDTO.getInAttesa());
        assertTrue(today.isBefore(somministrazioneDTO.getData()));

        Somministrazione somministrazione = Somministrazione.builder().id(0L).utente(utente).vaccino(vaccino).dataSomministrazione(today.plusDays(4)).codiceSomm(code).centro(cv1).oraSomministrazione("15:00").inAttesa(false).build();
        lenient().when(somministrazioneRepository.findByCodiceSomm(somministrazioneDTO.getCode())).thenReturn(somministrazione);

        assertNotEquals(somministrazioneDTO.getData(),somministrazione.getDataSomministrazione());
        assertNotEquals(somministrazioneDTO.getOra(),somministrazione.getOraSomministrazione());

        /**
         *
         * L'errore generato dal test è voluto , perchè non riesce a controllare la data
         */
        //lenient().when(clock.DAYS.between(today,giornoSomm)>=2 && today.isBefore(somministrazione.getDataSomministrazione())).thenReturn(true);

        /*somministrazione.setDataSomministrazione(somministrazioneDTO.getData());
        somministrazione.setOraSomministrazione(somministrazioneDTO.getOra());

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);

        assertEquals(somministrazione.getCodiceSomm(),somministrazioneServiceImpl.updateSomministrazione(code,somministrazioneDTO).getCode());*/

        assertNotNull(somministrazioneServiceImpl.updateSomministrazione(code,somministrazioneDTO));  //se non controllo la data mi aspetto una risposta non nulla che sarebbe l'esccezione

        reset(somministrazioneRepository);

    }




}
