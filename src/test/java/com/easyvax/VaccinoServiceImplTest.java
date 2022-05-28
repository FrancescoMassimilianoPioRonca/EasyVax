package com.easyvax;

import com.easyvax.dto.VaccinoDTO;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.VaccinoRepository;
import com.easyvax.service.impl.VaccinoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VaccinoServiceImplTest {

    private VaccinoServiceImpl vaccinoServiceImpl;

    @Mock
    private VaccinoRepository vaccinoRepository;

    @BeforeEach
    void setUp() {
        vaccinoServiceImpl = new VaccinoServiceImpl(vaccinoRepository);
    }

    /**
     * In questo metodo testo la funzionalità di ricerca del vaccino
     * in base al nome che l'utente inserisce.
     */
    @Test
    void findByNome(){

        String nome = "astrazeneca";
        Vaccino vaccino = Vaccino.builder().id(1L).nome(nome).casaFarmaceutica("Pfizer").build();

        lenient().when(vaccinoRepository.existsByNome(nome)).thenReturn(true);
        lenient().when(vaccinoRepository.findByNome(nome)).thenReturn(vaccino);

        VaccinoDTO vaccinoDTO = new VaccinoDTO(vaccino);
        assertNotNull(vaccinoDTO);
        assertEquals(vaccinoDTO.id,vaccinoServiceImpl.findByNome(nome).getId());

        verify(vaccinoRepository,atLeastOnce()).existsByNome(nome);
        verify(vaccinoRepository,atLeastOnce()).findByNome(nome);

        reset(vaccinoRepository);
    }

    /**
     * In questo metodo testo il corretto inserimento di un vaccino
     * nella base di dati
     */
    @Test
    void insertVaccino(){
        Long idVaccino = 0L;
        String nome = "Vaxzevria";
        String casaFarmaceutica="Astrazeneca";

        Vaccino vaccino = Vaccino.builder().id(idVaccino).casaFarmaceutica(casaFarmaceutica).nome(nome).build();

        VaccinoDTO vaccinoDTO = new VaccinoDTO(vaccino);

        lenient().when(vaccinoRepository.existsByNomeAndCasaFarmaceutica(nome,casaFarmaceutica)).thenReturn(false);
        lenient().when(vaccinoRepository.save(vaccino)).thenReturn(vaccino);
        assertEquals(vaccinoDTO.id,vaccinoServiceImpl.insertVaccino(vaccinoDTO).getId());

        reset(vaccinoRepository);

    }


    /**
     * In questo metodo testo il corretto update del vaccino in base all'id
     * Ho simulato di avere più vaccini nella base di dati crendo una lista di 2 vaccini.
     * Ho poi verificato che il vaccino che modifico sia quello corretto.
     */
    @Test
    void updateVaccino(){

        Long id = 0L;
        Vaccino vaccino = Vaccino.builder().id(id).casaFarmaceutica("test").nome("test").build();
        Vaccino vaccino1 = Vaccino.builder().id(2L).casaFarmaceutica("test").nome("test").build();

        lenient().when(vaccinoRepository.existsById(id)).thenReturn(true);
        lenient().when(vaccinoRepository.findById(id)).thenReturn(Optional.of(vaccino));

        VaccinoDTO vaccinoDTO = VaccinoDTO.builder().id(id).nome("test1").casaFarmaceutica("test1").build();

        lenient().when(vaccinoRepository.existsByNomeAndCasaFarmaceutica(vaccinoDTO.getNome(), vaccinoDTO.getCasaFarmaceutica())).thenReturn(false);

        vaccino.setNome(vaccinoDTO.nome);
        vaccino.setCasaFarmaceutica(vaccinoDTO.getCasaFarmaceutica());

        List<Vaccino> list = List.of(vaccino,vaccino1);

        lenient().when(vaccinoRepository.save(vaccino)).thenReturn(vaccino);
        lenient().when(vaccinoRepository.findAll()).thenReturn(list);

        assertEquals(list.get(0).getNome(),vaccinoServiceImpl.updateVaccino(vaccinoDTO).get(0).getNome());

        reset(vaccinoRepository);

    }

    /**
     * Questo metodo testa la corretta eliminazione
     * del vaccino con relativi controlli
     */
    @Test
    void deleteVaccino(){
        Long id = 0L;
        lenient().when(vaccinoRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(vaccinoServiceImpl.deleteVaccino(id));
        reset(vaccinoRepository);
    }

}
