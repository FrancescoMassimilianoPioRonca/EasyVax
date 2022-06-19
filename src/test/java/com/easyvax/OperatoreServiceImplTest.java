package com.easyvax;

import com.easyvax.dto.OperatoreDTO;
import com.easyvax.dto.PersonaleDTO;
import com.easyvax.dto.UtenteDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.model.*;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.OperatoreRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.OperatoreServiceImpl;
import com.easyvax.service.service.OperatoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperatoreServiceImplTest {

    private OperatoreServiceImpl operatoreServiceImpl;

    @Mock
    private OperatoreRepository operatoreRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private CentroVaccinaleRepository centroVaccinaleRepository;

    @BeforeEach
    void setUp() {
        operatoreServiceImpl = new OperatoreServiceImpl(operatoreRepository, utenteRepository, centroVaccinaleRepository);
    }


    /**
     * In questo metodo testo la ricerca dell'operatore in base
     * al codice fiscale
     */
    @Test
    void findByCodFiscale() {
        String cf = "RNCFNC030303E335M";
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale(cf).password("test").email("test@test.test").ruolo(RoleEnum.ROLE_OPERATOR).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("test").indirizzo("test").provincia(provincia).build();
        Operatore operatore = Operatore.builder().id(0L).utente(utente).centroVaccinale(cv).build();
        lenient().when(utenteRepository.existsByCodFiscale(cf)).thenReturn(true);
        lenient().when(operatoreRepository.findByCodFisc(cf)).thenReturn(operatore);

        OperatoreDTO operatoreDTO = new OperatoreDTO(operatore);
        assertNotNull(operatoreDTO);

        assertEquals(operatoreDTO.getIdUtente(), operatoreServiceImpl.findByCodFiscale(cf).getIdUtente());

        reset(utenteRepository);
        reset(operatoreRepository);
    }

    /**
     * In questo metodo,testo l'inserimento dell'operatore con i relativi controlli
     */
    @Test
    void insertOperatore() {
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").password("test").email("test@test.test").ruolo(RoleEnum.ROLE_OPERATOR).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("test").indirizzo("test").provincia(provincia).build();
        Operatore operatore = Operatore.builder().id(0L).utente(utente).centroVaccinale(cv).build();

        lenient().when(operatoreRepository.existsByUtente_Id(operatore.getUtente().getId())).thenReturn(false);
        lenient().when(utenteRepository.findById(operatore.getUtente().getId())).thenReturn(Optional.of(utente));
        lenient().when(centroVaccinaleRepository.findById(operatore.getCentroVaccinale().getId())).thenReturn(Optional.of(cv));

        lenient().when(operatoreRepository.save(operatore)).thenReturn(operatore);

        OperatoreDTO operatoreDTO = new OperatoreDTO(operatore);
        assertNotNull(operatoreDTO);

        assertEquals(operatoreDTO.getIdUtente(), operatoreServiceImpl.insertOperatore(operatoreDTO).getIdUtente());

        reset(utenteRepository);
        reset(operatoreRepository);
        reset(centroVaccinaleRepository);
    }


    /**
     * In questo metodo, testo la cancellazione dell'operatore in abse all'id
     * con i relativi controlli
     */
    @Test
    void deleteOperatore() {

        Long id = 1L;

        lenient().when(operatoreRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(operatoreServiceImpl.deleteOperatore(id));
        verify(operatoreRepository, atLeastOnce()).existsById(id);
        reset(operatoreRepository);


    }

    /**
     * In questo metodo testo l'update dell'operatore.
     * Come negli altri update, simulo la presenza di più operatori nella base di dati
     * per poi verificare che quello che modifico sia quello corretto
     */
    @Test
    void updateOperatore(){

        Long id = 9L;
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").password("test").email("test@test.test").ruolo(RoleEnum.ROLE_OPERATOR).build();
        Utente utente2 = Utente.builder().id(2l).nome("Test").cognome("test").codFiscale("test2").password("test").email("test2@test.test").ruolo(RoleEnum.ROLE_OPERATOR).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(4L).nome("test2").indirizzo("test2").provincia(provincia).build();
        Operatore operatore = Operatore.builder().id(id).utente(utente).centroVaccinale(cv1).build();
        Operatore operatore1 = Operatore.builder().id(30L).utente(utente2).centroVaccinale(cv1).build();


        OperatoreDTO operatoreDTO = OperatoreDTO.builder().id(id).idUtente(utente2.getId()).idCentro(cv1.getId()).build();

        lenient().when(operatoreRepository.existsById(operatoreDTO.id)).thenReturn(true);

        lenient().when(utenteRepository.existsById(operatoreDTO.getIdUtente())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.existsById(operatoreDTO.getIdCentro())).thenReturn(true);

        lenient().when(utenteRepository.findById(utente2.getId())).thenReturn(Optional.of(utente2));
        lenient().when(centroVaccinaleRepository.findById(cv1.getId())).thenReturn(Optional.of(cv1));


        operatore.setUtente(utente2);
        operatore.setCentroVaccinale(cv1);

        lenient().when(operatoreRepository.save(operatore)).thenReturn(operatore);

        List<Operatore> list = List.of(operatore,operatore1);

        lenient().when(operatoreRepository.findAll()).thenReturn(list);

        assertEquals(list.get(0).getUtente().getId(),operatoreServiceImpl.updateOperatore(operatoreDTO).get(0).getIdUtente());

        reset(centroVaccinaleRepository);
        reset(operatoreRepository);
        reset(utenteRepository);

    }


}
