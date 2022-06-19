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


    /**
     * Questo metodo testa la corretta visualizzazione delle richieste non ancora smarcate
     * nel centro vaccinale in cui lavora l'operatore
     */
    @Test
    public void getRichiesteOperatoreTest(){

        Long id = 0L;
        LocalDate date = LocalDate.now().minusDays(2);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        Operatore operatore = Operatore.builder().id(id).utente(utente).centroVaccinale(cv1).build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(3L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).oldCentroVacc(cv1).build();
        Richiesta richiesta2 = Richiesta.builder().id(10L).somministrazione(somministrazione).oldCentroVacc(cv1).build();

        List<Richiesta> list = List.of(richiesta,richiesta2);

        Assertions.assertNotNull(id);
        lenient().when(operatoreRepository.existsById(operatore.getId())).thenReturn(true);
        lenient().when(operatoreRepository.findById(id)).thenReturn(Optional.of(operatore));
        lenient().when(centroVaccinaleRepository.findById(somministrazione.getCentro().getId())).thenReturn(Optional.ofNullable(cv1));
        lenient().when(richiestaRepository.getRichieste(cv1.getId())).thenReturn(list);

        assertNotNull(list);

        assertEquals(list.get(0).getId(),richiestaServiceImpl.getRichiesteOperatore(id).get(0).getId());

        reset(operatoreRepository);
        reset(richiestaRepository);

    }

    /**
     * Questo metodo testa il corretto inserimento di una richiesta per cambio data.
     * Siccome il metodo del service era molto complesso l'ho suddiviso in più metodi da testare
     */
    @Test
    public void insertRichiestaCambioData(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newData(date).oldCentroVacc(cv1).build();

        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.ofNullable(somministrazione));
        somministrazione.setInAttesa(true);


        assertEquals(0,richiestaRepository.findBySomministrazione_IdAndApproved(somministrazione.getId()));
        lenient().when(richiestaRepository.findBySomministrazione_IdAndApproved(somministrazione.getId())).thenReturn(0);
        Assertions.assertNotNull(richiesta.getNewData());
        Assertions.assertNull(richiesta.getNewCentro());

        lenient().when(centroVaccinaleRepository.findById(somministrazione.getCentro().getId())).thenReturn(Optional.ofNullable(cv1));

        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);
        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);
        assertEquals(richiesta.getNewData(),richiestaServiceImpl.insertRichiesta(richiestaDTO).getData());

        reset(richiestaRepository);
        reset(somministrazioneRepository);


    }

    /**
     * Questo metodo testa l'inserimento della richiesta per cambio sede
     */
    @Test
    public void insertRichiestaCambioSede(){

        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        CentroVaccinale cv2 = CentroVaccinale.builder().id(4L).nome("prova2").provincia(provincia).indirizzo("prova2").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newCentro(cv2.getId()).oldCentroVacc(cv1).build();

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));
        somministrazione.setInAttesa(true);

        assertEquals(0,richiestaRepository.findBySomministrazione_IdAndApproved(somministrazione.getId()));
        lenient().when(richiestaRepository.findBySomministrazione_IdAndApproved(somministrazione.getId())).thenReturn(0);
        lenient().when(centroVaccinaleRepository.existsById(richiesta.getNewCentro())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.findById(somministrazione.getCentro().getId())).thenReturn(Optional.of(cv1));

        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        assertEquals(richiesta.getNewCentro(),richiestaServiceImpl.insertRichiesta(richiestaDTO).getIdNewcentro());

        reset(richiestaRepository);
        reset(centroVaccinaleRepository);
        reset(somministrazioneRepository);


    }

    /**
     * Questo metodo testa l'accettazione da parte dell'operatore di una richiesta per cambio data.
     */
    @Test
    public void accettaRichiestaNewData(){

        Long idOperator=0L;
        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();
        Operatore op1 = Operatore.builder().id(idOperator).centroVaccinale(cv1).build();
        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newData(date).oldCentroVacc(cv1).idOp1(1L).build();

        lenient().when(richiestaRepository.existsById(richiesta.getId())).thenReturn(true);
        lenient().when(operatoreRepository.existsById(idOperator)).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiesta.getId())).thenReturn(Optional.of(richiesta));
        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        assertNotEquals(richiesta.getIdOp1(),idOperator);
        lenient().when(operatoreRepository.checkOperatore(somministrazione.getCentro().getId(),idOperator)).thenReturn(1);

        Assertions.assertNotNull(richiesta.getNewData());
        Assertions.assertNull(richiesta.getNewCentro());

        somministrazione.setInAttesa(false);
        richiesta.setApproved(true);


        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        //Assertions.assertNotNull(richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId()));


        assertEquals(true,richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId(),idOperator));

        reset(richiestaRepository);
        reset(somministrazioneRepository);


    }


    /**
     * Questo metodo testa la richiesta di cambio sede quando l'operatore della sede iniziale ha già appaorvato lo spostamento,
     * che per essere ultimato deve essere approvato anche dall'operatore della sede in cui si vuole effettuare il vaccino
     */
    @Test
    public void accettaRichiestaNewCentroOP2(){

        Long idOperatore = 3L;
        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        CentroVaccinale cv2 = CentroVaccinale.builder().id(5L).nome("prova2").provincia(provincia).indirizzo("prova2").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newCentro(cv1.getId()).oldCentroVacc(cv2).approvedOp1(true).idOp1(1L).build();
        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        Assertions.assertNotNull(richiestaDTO.id);
        lenient().when(richiestaRepository.existsById(richiestaDTO.id)).thenReturn(true);
        lenient().when(operatoreRepository.existsById(idOperatore)).thenReturn(true);

        lenient().when(richiestaRepository.findById(richiestaDTO.id)).thenReturn(Optional.ofNullable(richiesta));

        lenient().when(somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())).thenReturn(true);

        lenient().when(somministrazioneRepository.findById(somministrazione.getId())).thenReturn(Optional.of(somministrazione));

        Assertions.assertNotNull(richiestaDTO.getIdNewcentro());
        Assertions.assertNull(richiestaDTO.getData());
        Assertions.assertNotNull(richiestaDTO.getIdOp1());
        Assertions.assertNull(richiestaDTO.approvedOp2);
        Assertions.assertNotEquals(richiestaDTO.idOp2,idOperatore);
        lenient().when(operatoreRepository.checkOperatore(richiestaDTO.getIdNewcentro(),idOperatore)).thenReturn(1);

        lenient().when(centroVaccinaleRepository.findById(richiestaDTO.getIdNewcentro())).thenReturn(Optional.of(cv1));


        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        assertEquals(true, richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId(),idOperatore));

        reset(somministrazioneRepository);
        reset(operatoreRepository);
        reset(centroVaccinaleRepository);
        reset(richiestaRepository);

    }

    /**
     * Questo metodo testa l'accettazione di cambio sede dell'operatore 1 ovvero dell'operatore della sede iniziale della somministrazione
     */
    @Test
    public void accettaRichiestaNewCentroOP1(){

        Long idOperatore=8L;
        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        CentroVaccinale cv2 = CentroVaccinale.builder().id(5L).nome("prova2").provincia(provincia).indirizzo("prova2").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).newCentro(cv1.getId()).oldCentroVacc(cv2).idOp1(idOperatore).build();

        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);
        richiestaDTO.approvedOp1=null;

        lenient().when(richiestaRepository.existsById(richiestaDTO.getId())).thenReturn(true);
        lenient().when(operatoreRepository.existsById(idOperatore)).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiestaDTO.getId())).thenReturn(Optional.of(richiesta));
        lenient().when(somministrazioneRepository.existsById(richiestaDTO.idSomministrazione)).thenReturn(true);
        lenient().when(somministrazioneRepository.findById(richiestaDTO.idSomministrazione)).thenReturn(Optional.of(somministrazione));

        Assertions.assertNotNull(richiesta.getNewCentro());
        Assertions.assertNull(richiesta.getNewData());
        assertNull(richiesta.getApprovedOp1());
        lenient().when(centroVaccinaleRepository.existsById(somministrazione.getCentro().getId())).thenReturn(true);
        //Assertions.assertNull(richiesta.get());
        lenient().when(operatoreRepository.checkOperatore(somministrazione.getCentro().getId(),richiestaDTO.idOp1)).thenReturn(1);


        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);
        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        assertEquals(true,richiestaServiceImpl.accettaRichiesta(richiestaDTO.getId(),idOperatore));


        reset(richiestaRepository);
        reset(centroVaccinaleRepository);
        reset(somministrazioneRepository);

    }

    /**
     * Questo metodo testa l'operazione di rifiuto di una richiesta da parte di un operatore
     */
    @Test
    public void rifiutaRichiesta(){

        Long idOperatore=3L;
        LocalDate date = LocalDate.now();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).password("abcde").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(3L).nome("prova1").provincia(provincia).indirizzo("prova").build();
        Vaccino vaccino = Vaccino.builder().id(1L).nome("test").casaFarmaceutica("Pfizer").build();
        Somministrazione somministrazione = Somministrazione.builder().id(6L).utente(utente).vaccino(vaccino).dataSomministrazione(date).centro(cv1).oraSomministrazione("15:00").codiceSomm("test").build();

        Richiesta richiesta = Richiesta.builder().id(10L).somministrazione(somministrazione).oldCentroVacc(cv1).build();
        RichiestaDTO richiestaDTO = new RichiestaDTO(richiesta);

        lenient().when(richiestaRepository.existsById(richiestaDTO.getId())).thenReturn(true);
        lenient().when(operatoreRepository.existsById(idOperatore)).thenReturn(true);
        lenient().when(richiestaRepository.findById(richiestaDTO.getId())).thenReturn(Optional.of(richiesta));

       lenient().when(operatoreRepository.checkOperatore(richiestaDTO.idOldCentro,idOperatore)).thenReturn(1);

        lenient().when(richiestaRepository.save(richiesta)).thenReturn(richiesta);

        lenient().when(somministrazioneRepository.findById(richiesta.getSomministrazione().getId())).thenReturn(Optional.of(somministrazione));

        somministrazione.setInAttesa(false);

        lenient().when(somministrazioneRepository.save(somministrazione)).thenReturn(somministrazione);


        assertEquals(true,richiestaServiceImpl.rifiutaRichiesta(richiestaDTO.getId(),idOperatore));

        reset(richiestaRepository);
        reset(somministrazioneRepository);
    }

}
