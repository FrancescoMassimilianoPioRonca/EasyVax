package com.easyvax;

import com.easyvax.dto.CentroVaccinaleDTO;
import com.easyvax.dto.OperatoreDTO;
import com.easyvax.dto.PersonaleDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.model.*;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.PersonaleRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.impl.PersonaleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
public class PersonaleServiceImplTest {

    private PersonaleServiceImpl personaleServiceImpl;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private PersonaleRepository personaleRepository;

    @Mock
    private CentroVaccinaleRepository centroVaccinaleRepository;

    @BeforeEach
    void setUp() {
        personaleServiceImpl = new PersonaleServiceImpl(personaleRepository, utenteRepository, centroVaccinaleRepository);
    }

    /**
     * In questo metodo, testo la corretta icerca del personale di una struttura in base al codfis
     */
    @Test
    void findByCodFiscale() {
        String cf = "RNCFNC030303E335M";
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale(cf).password("test").email("test@test.test").ruolo(RoleEnum.ROLE_PERSONALE).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("test").indirizzo("test").provincia(provincia).build();
        Personale personale = Personale.builder().id(0L).utente(utente).centroVaccinale(cv).build();

        assertNotNull(cf);
        lenient().when(utenteRepository.existsByCodFiscale(cf)).thenReturn(true);
        lenient().when(personaleRepository.findByCodFisc(cf)).thenReturn(personale);

        PersonaleDTO personaleDTO = new PersonaleDTO(personale);
        assertNotNull(personaleDTO);

        assertEquals(personaleDTO.getIdUtente(), personaleRepository.findByCodFisc(cf).getUtente().getId());

        //System.out.println(personale.getUtente().getCodFiscale());
        reset(utenteRepository);
        reset(personaleRepository);
    }

    /**
     * In questo metodo, testo il corretto inserimento del personale con i relativi controlli
     */
    @Test
    void insertPersonale() {
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").password("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("test").indirizzo("test").provincia(provincia).build();
        Personale personale = Personale.builder().id(0L).utente(utente).centroVaccinale(cv).build();

        lenient().when(personaleRepository.existsByUtente_Id(personale.getId())).thenReturn(false);
        lenient().when(utenteRepository.findById(personale.getUtente().getId())).thenReturn(Optional.of(utente));
        lenient().when(centroVaccinaleRepository.findById(personale.getCentroVaccinale().getId())).thenReturn(Optional.of(cv));

        personale.getUtente().setRuolo(RoleEnum.ROLE_PERSONALE);
        lenient().when(personaleRepository.save(personale)).thenReturn(personale);

        PersonaleDTO personaleDTO = new PersonaleDTO(personale);
        assertNotNull(personaleDTO);

        assertEquals(personaleDTO.getIdUtente(), personaleServiceImpl.insertpersonale(personaleDTO).getIdUtente());

        reset(utenteRepository);
        reset(personaleRepository);
    }

    /**
     * In questo metodo, testo la corretta eliminazione di un personale
     */
    @Test
    void deletePersonale() {

        Long id = 1L;

        lenient().when(personaleRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(personaleServiceImpl.deletePersonale(id));
        verify(personaleRepository, atLeastOnce()).existsById(id);
        reset(personaleRepository);


    }

    /**
     * In questo metodo testo l'update del centroVaccinale.
     * Come negli altri update, simulo la presenza d più centri nella base di dati
     * per poi verificare che quello che modifico sia quello corretto
     */
    @Test
    void updatePersonale(){
        Long id = 0L;
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").password("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).build();
        Utente utente2 = Utente.builder().id(2l).nome("Test").cognome("test").codFiscale("test2").password("test").email("test2@test.test").ruolo(RoleEnum.ROLE_USER).build();
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("test").indirizzo("test").provincia(provincia).build();
        CentroVaccinale cv1 = CentroVaccinale.builder().id(4L).nome("test2").indirizzo("test2").provincia(provincia).build();
        Personale personale = Personale.builder().id(id).utente(utente).centroVaccinale(cv).build();
        Personale personale2 = Personale.builder().id(id).utente(utente2).centroVaccinale(cv).build();

        PersonaleDTO personaleDTO = PersonaleDTO.builder().id(id).idUtente(utente2.getId()).idCentro(cv1.getId()).build();

        lenient().when(personaleRepository.existsById(personaleDTO.id)).thenReturn(true);
        lenient().when(utenteRepository.findById(personaleDTO.getIdUtente())).thenReturn(Optional.of(utente));
        lenient().when(centroVaccinaleRepository.findById(personaleDTO.getIdCentro())).thenReturn(Optional.of(cv));

        lenient().when(utenteRepository.existsById(utente.getId())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.existsById(cv.getId())).thenReturn(true);


        personale.setUtente(utente2);
        personale.setCentroVaccinale(cv1);

        lenient().when(personaleRepository.save(personale)).thenReturn(personale);

        List<Personale> list = List.of(personale,personale2);

        lenient().when(personaleRepository.findAll()).thenReturn(list);

        assertEquals(list.get(0).getUtente().getId(),personaleServiceImpl.updatePersonale(personaleDTO).get(0).getIdUtente());

        reset(centroVaccinaleRepository);
        reset(personaleRepository);
        reset(utenteRepository);

    }
}
