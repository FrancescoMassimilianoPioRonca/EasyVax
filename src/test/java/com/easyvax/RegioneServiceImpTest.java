package com.easyvax;

import com.easyvax.dto.RegioneDTO;
import com.easyvax.dto.VaccinoDTO;
import com.easyvax.model.Regione;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.RegioneServiceImpl;
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

    /**
     * In questo metodo, testo la ricerca di una regione in base al nome
     */
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

    /**
     * In questo metodo testo il corretto inserimento della regione
     * nella base di dati
     */
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

    /**
     * In questo metodo testo il corretto update della regione.
     * Ho simulato di avere più regioni nella base di dati crendo una lista di 2 regioni.
     * Ho poi verificato che la regione che modifico sia quella corretto.
     */
    @Test
    void updateRegione(){

        Long id = 0L;
        Regione regione = Regione.builder().id(id).nome("Molise").build();
        Regione regione1 = Regione.builder().id(1L).nome("Lazio").build();

        lenient().when(regioneRepository.existsById(id)).thenReturn(true);
        lenient().when(regioneRepository.findById(id)).thenReturn(Optional.of(regione));

        RegioneDTO regioneDTO = RegioneDTO.builder().id(id).nome("test1").build();

        lenient().when(regioneRepository.existsByNome(regioneDTO.getNome())).thenReturn(false);

        regione.setNome(regioneDTO.nome);

        List<Regione> list = List.of(regione,regione1);

        lenient().when(regioneRepository.save(regione)).thenReturn(regione);
        lenient().when(regioneRepository.findAll()).thenReturn(list);

        assertEquals(list.get(0).getNome(),regioneServiceImpl.updateRegione(regioneDTO).get(0).getNome());

        reset(regioneRepository);

    }

    /**
     * Questo metodo testa la corretta eliminazione
     * della regione con relativi controlli
     */
    @Test
    void deleteRegione(){
        Long id = 0L;
        lenient().when(regioneRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(regioneServiceImpl.deleteRegione(id));
        reset(regioneRepository);
    }

}
