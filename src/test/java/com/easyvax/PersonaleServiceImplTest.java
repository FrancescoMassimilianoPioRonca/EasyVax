package com.easyvax;

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

    @Test
    void deletePersonale() {

        Long id = 1L;

        lenient().when(personaleRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(personaleServiceImpl.deletePersonale(id));
        verify(personaleRepository, atLeastOnce()).existsById(id);
        reset(personaleRepository);


    }
}
