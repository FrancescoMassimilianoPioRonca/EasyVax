package com.easyvax;

import com.easyvax.dto.RegioneDTO;
import com.easyvax.dto.VaccinoDTO;
import com.easyvax.model.Regione;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.RegioneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
public class RegioneServiceImpTest {

    private RegioneServiceImpl regioneServiceImpl;

    @Mock
    private RegioneRepository regioneRepository;

    @Mock
    private ProvinciaRepository provinciaRepository;

    @BeforeEach
    void setUp() {
        regioneServiceImpl = new RegioneServiceImpl(regioneRepository,provinciaRepository);
    }

    @Test
    void findByNome(){

        String nome = "Molise";
        Regione regione = Regione.builder().id(2L).nome(nome).build();

        lenient().when(regioneRepository.existsByNome(nome)).thenReturn(true);
        lenient().when(regioneRepository.findByNome(nome)).thenReturn(regione);

        RegioneDTO regioneDTO = new RegioneDTO(regione);
        assertNotNull(regioneDTO);

        assertEquals(regioneDTO.id,regioneServiceImpl.findByNome(nome).getId());

        reset(regioneRepository);
    }

    @Test
    void insertRegione(){

        Regione regione = Regione.builder().id(0L).nome("Molise").build();

        lenient().when(regioneRepository.existsByNome(regione.getNome())).thenReturn(false);
        assertNotNull(regione.getNome());

        RegioneDTO  regioneDTO = new RegioneDTO(regione);
        assertNotNull(regioneDTO);

        lenient().when(regioneRepository.save(regione)).thenReturn(regione);

        assertEquals(regioneDTO.getId(),regioneServiceImpl.insertRegione(regioneDTO).getId());

        reset(regioneRepository);

    }

}
