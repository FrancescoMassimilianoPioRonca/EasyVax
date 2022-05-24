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
}
