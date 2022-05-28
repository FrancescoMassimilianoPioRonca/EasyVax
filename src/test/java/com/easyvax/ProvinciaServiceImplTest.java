package com.easyvax;

import com.easyvax.dto.CentroVaccinaleDTO;
import com.easyvax.dto.ProvinciaDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Regione;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.ProvinciaServiceImpl;
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
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
public class ProvinciaServiceImplTest {

    private ProvinciaServiceImpl provinciaServiceImpl;

    @Mock
    private RegioneRepository regioneRepository;

    @Mock
    private ProvinciaRepository provinciaRepository;

    @BeforeEach
    void setUp() {
        provinciaServiceImpl = new ProvinciaServiceImpl(provinciaRepository, regioneRepository);
    }

    /**
     * In questo metodo testo la ricerca della provincia in base al cap
     */
    @Test
    void findByCap() {

        String cap = "00179";
        Regione regione = Regione.builder().id(1L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(0L).nome("Roma").cap(cap).regione(regione).build();

        lenient().when(provinciaRepository.existsByCap(cap)).thenReturn(true);
        lenient().when(provinciaRepository.findByCap(cap)).thenReturn(provincia);

        ProvinciaDTO provinciaDTO = new ProvinciaDTO(provincia);
        assertNotNull(provinciaDTO);

        assertEquals(provinciaDTO.getId(), provinciaServiceImpl.findByCap(cap).getId());

        reset(provinciaRepository);
    }

    /**
     * In questo metodo testo l'inserimento di una nuova provincia con tutti i controlli
     */
    @Test
    void insertProvincia() {

        Regione regione = Regione.builder().id(1L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(9L).nome("Roma").cap("00159").build();

        lenient().when(provinciaRepository.existsByNomeAndRegione_IdAndCap(provincia.getNome(), regione.getId(), provincia.getCap())).thenReturn(false);
        lenient().when(regioneRepository.findById(regione.getId())).thenReturn(Optional.of(regione));

        provincia.setRegione(regione);

        ProvinciaDTO provinciaDTO = new ProvinciaDTO(provincia);

        lenient().when(provinciaRepository.save(provincia)).thenReturn(provincia);
        assertEquals(provinciaDTO.getId(), provinciaServiceImpl.insertProvincia(provinciaDTO).getId());

        reset(provinciaRepository);
        reset(regioneRepository);
    }

    /**
     * In questo metodo testo l'update della provincia.
     * Come negli altri update, simulo la presenza d più provincie nella base di dati
     * per poi verificare che quella che modifico sia quella corretto
     */
    @Test
    void updateProvincia() {
        Long id = 0L;
        Regione regione = Regione.builder().id(1L).nome("Lazio").build();
        Provincia provincia1 = Provincia.builder().id(id).nome("Roma").cap("00159").regione(regione).build();
        Provincia provincia2 = Provincia.builder().id(1L).nome("Roma").cap("00179").regione(regione).build();

        lenient().when(provinciaRepository.existsById(id)).thenReturn(true);

        lenient().when(provinciaRepository.findById(id)).thenReturn(Optional.of(provincia1));
        lenient().when(regioneRepository.findById(regione.getId())).thenReturn(Optional.of(regione));

        ProvinciaDTO provinciaDTO = ProvinciaDTO.builder().id(id).nome("TEST").cap("test").idRegione(regione.getId()).build();

        lenient().when(provinciaRepository.existsByNomeAndRegione_IdAndCap(provinciaDTO.getNome(), provinciaDTO.getIdRegione(), provinciaDTO.getCap())).thenReturn(false);

        provincia1.setNome(provinciaDTO.nome);
        provincia1.setCap(provinciaDTO.cap);

        lenient().when(provinciaRepository.save(provincia1)).thenReturn(provincia1);

        List<Provincia> list = List.of(provincia1, provincia2);

        lenient().when(provinciaRepository.findAll()).thenReturn(list);

        assertEquals(list.get(0).getCap(), provinciaServiceImpl.updateProvincia(provinciaDTO).get(0).getCap());

        reset(regioneRepository);
        reset(provinciaRepository);

    }

    /**
     * In questo metodo testo la corretta eliminazione della provincia in base all'id
     */
    @Test
    void deleteProvincia() {

        Long id = 0L;
        lenient().when(provinciaRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(provinciaServiceImpl.deleteProvincia(id));
        reset(provinciaRepository);

    }


}
