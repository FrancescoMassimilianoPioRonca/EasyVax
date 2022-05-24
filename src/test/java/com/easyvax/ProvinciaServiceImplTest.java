package com.easyvax;

import com.easyvax.dto.ProvinciaDTO;
import com.easyvax.model.Provincia;
import com.easyvax.model.Regione;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.ProvinciaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        provinciaServiceImpl = new ProvinciaServiceImpl(provinciaRepository,regioneRepository);
    }

    @Test
    void findByCap(){

        String cap = "00179";
        Regione regione = Regione.builder().id(1L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(0L).nome("Roma").cap(cap).regione(regione).build();

        lenient().when(provinciaRepository.existsByCap(cap)).thenReturn(true);
        lenient().when(provinciaRepository.findByCap(cap)).thenReturn(provincia);

        ProvinciaDTO provinciaDTO = new ProvinciaDTO(provincia);
        assertNotNull(provinciaDTO);

        assertEquals(provinciaDTO.getId(),provinciaServiceImpl.findByCap(cap).getId());

        reset(provinciaRepository);
    }

    @Test
    void insertProvincia(){

        Regione regione = Regione.builder().id(1L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(9L).nome("Roma").cap("00159").build();

        lenient().when(provinciaRepository.existsByNomeAndRegione_IdAndCap(provincia.getNome(),regione.getId(),provincia.getCap())).thenReturn(false);
        lenient().when(regioneRepository.findById(regione.getId())).thenReturn(Optional.of(regione));

        provincia.setRegione(regione);

        ProvinciaDTO provinciaDTO = new ProvinciaDTO(provincia);

        lenient().when(provinciaRepository.save(provincia)).thenReturn(provincia);
       assertEquals(provinciaDTO.getId(),provinciaServiceImpl.insertProvincia(provinciaDTO).getId());

        reset(provinciaRepository);
        reset(regioneRepository);
    }
}
