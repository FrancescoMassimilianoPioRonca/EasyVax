package com.easyvax;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.impl.OperatoreServiceImpl;
import com.easyvax.service.impl.RichiestaServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
public class RichiesteServiceimplTest {

    private RichiestaServiceImpl richiestaServiceImpl;

    @Mock
    private  CentroVaccinaleRepository centroVaccinaleRepository;

    @Mock
    private  RichiestaRepository richiestaRepository;

    @Mock
    private  SomministrazioneRepository somministrazioneRepository;

    @Mock
    private  UtenteRepository utenteRepository;

    @Mock
    private OperatoreRepository operatoreRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        richiestaServiceImpl = new RichiestaServiceImpl(operatoreRepository, centroVaccinaleRepository, richiestaRepository,somministrazioneRepository,utenteRepository,javaMailSender);
    }


    @Test
    public void getRichiesteOperatoreTest(){

        LocalDate date = LocalDate.now().minusDays(2);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        Operatore operatore = Operatore.builder().id(0L).utente(utente).centroVaccinale(cv1).build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).build();

        List<Richiesta> list = List.of(richiesta);

        lenient().when(operatoreRepository.existsById(operatore.getId())).thenReturn(true);
        lenient().when(richiestaRepository.getRichieste(operatore.getId())).thenReturn(list);

        assertNotNull(list);

        assertEquals(list.get(0).getId(),richiestaServiceImpl.getRichiesteOperatore(operatore.getId()).get(0).getId());

        reset(operatoreRepository);
        reset(richiestaRepository);

    }

    @Test
    public void insertRichiestaCambioData(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newData(date).build();

        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));
        somministrazione.setInAttesa(true);

        lenient().when(richiestaRepository.existsBySomministrazione_Id(somministrazione.getId())).thenReturn(false);
        Assertions.assertNotNull(richiesta.getNewData());
        Assertions.assertNull(richiesta.getIdCentroVacc());

        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);
        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);
        assertEquals(richiesta.getNewData(),richiestaServiceImpl.insertRichiesta(richiestaDTO).getData());

        reset(richiestaRepository);
        reset(somministrazioneRepository);


    }

    @Test
    public void insertRichiestaCambioSede(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).IdCentroVacc(cv1.getId()).build();

        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));
        somministrazione.setInAttesa(true);

        lenient().when(richiestaRepository.existsBySomministrazione_Id(somministrazione.getId())).thenReturn(false);
        lenient().when(centroVaccinaleRepository.existsById(richiesta.getIdCentroVacc())).thenReturn(true);
        Assertions.assertNotNull(richiesta.getIdCentroVacc());
        Assertions.assertNull(richiesta.getNewData());
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);



        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);
        assertEquals(richiesta.getIdCentroVacc(),richiestaServiceImpl.insertRichiesta(richiestaDTO).getIdCentroVaccinale());

        reset(richiestaRepository);
        reset(centroVaccinaleRepository);
        reset(somministrazioneRepository);


    }

    @Test
    public void accettaRichiestaNewData(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newData(date).build();

        lenient().when(richiestaRepository.existsById(richiesta.getId())).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiesta.getId())).thenReturn(Optional.of(richiesta));
        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        Assertions.assertNotNull(richiesta.getNewData());
        Assertions.assertNull(richiesta.getIdCentroVacc());

        somministrazione.setInAttesa(false);
        richiesta.setApproved(true);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        //Assertions.assertNotNull(richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId()));

        assertEquals(true,richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId()));

        reset(richiestaRepository);
        reset(somministrazioneRepository);


    }


    /**
     * Questo metodo testa la richiesta di cambio sede quando l'operatore della sede iniziale ha già appaorvato lo spostamento,
     * che per essere ultimato deve essere approvato anche dall'operatore della sede in cui si vuole effettuare il vaccino
     */
    @Test
    public void accettaRichiestaNewCentroOP2(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).IdCentroVacc(cv1.getId()).approvedOp1(true).build();

        lenient().when(richiestaRepository.existsById(richiesta.getId())).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiesta.getId())).thenReturn(Optional.of(richiesta));
        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        Assertions.assertNotNull(richiesta.getIdCentroVacc());
        Assertions.assertNull(richiesta.getNewData());

        Assertions.assertNotNull(richiesta.getApprovedOp1());
        Assertions.assertNull(richiesta.getApprovedOp2());

        //richiesta.setApprovedOp2(true);
        richiesta.setApproved(true);
        somministrazione.setInAttesa(false);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        assertEquals(true,richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId()));

        reset(richiestaRepository);
        reset(somministrazioneRepository);


    }

    /**
     * Questo metodo testa l'accettazione di cambio sede dell'operatore 1 ovvero dell'operatore della sede iniziale della somministrazione
     */
    @Test
    public void accettaRichiestaNewCentroOP1(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).IdCentroVacc(cv1.getId()).approvedOp1(true).build();

        lenient().when(richiestaRepository.existsById(richiesta.getId())).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiesta.getId())).thenReturn(Optional.of(richiesta));
        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        Assertions.assertNotNull(richiesta.getIdCentroVacc());
        Assertions.assertNull(richiesta.getNewData());

        lenient().when(centroVaccinaleRepository.existsById(richiesta.getIdCentroVacc())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.findById(richiesta.getIdCentroVacc())).thenReturn(Optional.of(cv1));

        somministrazione.setCentro(cv1);
        richiesta.setApprovedOp1(true);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        assertEquals(true,richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId()));


        reset(richiestaRepository);
        reset(centroVaccinaleRepository);
        reset(somministrazioneRepository);

    }

    /**
     * Questo metodo testa l'operazione di rifiuto di una richiesta da parte di un operatore
     */
    @Test
    public void rifiutaRichiesta(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).build();

        lenient().when(richiestaRepository.existsById(richiesta.getId())).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiesta.getId())).thenReturn(Optional.of(richiesta));

        richiesta.setApproved(false);

        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        somministrazione.setInAttesa(false);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);

        assertEquals(true,richiestaServiceImpl.rifiutaRichiesta(richiesta.getId()));

        reset(richiestaRepository);
        reset(somministrazioneRepository);
    }

}
