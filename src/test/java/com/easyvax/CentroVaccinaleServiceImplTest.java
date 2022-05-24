package com.easyvax;

import com.easyvax.dto.CentroVaccinaleDTO;
import com.easyvax.dto.ProvinciaDTO;
import com.easyvax.dto.VaccinoDTO;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.RegioneRepository;
import com.easyvax.repository.VaccinoRepository;
import com.easyvax.service.impl.CentroVaccinaleServiceImpl;
import com.easyvax.service.impl.VaccinoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CentroVaccinaleServiceImplTest {


    private CentroVaccinaleServiceImpl centroVaccinaleServiceImpl;

    @Mock
    private CentroVaccinaleRepository centroVaccinaleRepository;

    @Mock
    private RegioneRepository regioneRepository;

    @Mock
    private ProvinciaRepository provinciaRepository;

    @Mock
    private VaccinoRepository vaccinoRepository;

    @BeforeEach
    void setUp() {
        centroVaccinaleServiceImpl = new CentroVaccinaleServiceImpl(centroVaccinaleRepository,vaccinoRepository,regioneRepository,provinciaRepository);
    }


    @Test
    void findByProvincia(){

        Provincia provincia = Provincia.builder().nome("Molise").id(1L).cap("86170").build();
        CentroVaccinale cv1 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        CentroVaccinale cv2 = CentroVaccinale.builder().nome("prova1").provincia(provincia).indirizzo("prova").build();
        List<CentroVaccinale> list = List.of(cv1,cv2);
       // List<CentroVaccinaleDTO> listDTO = list.stream().map(CentroVaccinaleDTO::new).collect(Collectors.toList());

        lenient().when(provinciaRepository.existsById(provincia.getId())).thenReturn(true);
        lenient().when(centroVaccinaleRepository.findByProvincia_Id(provincia.getId())).thenReturn(list);


        assertEquals(list.get(0).getNome(),centroVaccinaleServiceImpl.findByProvincia(provincia.getId()).get(0).getNome());

        reset(centroVaccinaleRepository);
        reset(provinciaRepository);

    }

    @Test
    void insertCentro(){

        Provincia provincia = Provincia.builder().nome("Molise").id(1L).cap("86170").build();
        CentroVaccinale cv = CentroVaccinale.builder().id(3L).nome("prova1").indirizzo("prova").build();

        lenient().when(provinciaRepository.findById(provincia.getId())).thenReturn(Optional.of(provincia));
        lenient().when(centroVaccinaleRepository.existsByNomeAndProvincia_Id(cv.getNome(), provincia.getId())).thenReturn(false);
        lenient().when(provinciaRepository.existsById(provincia.getId())).thenReturn(true);

        cv.setProvincia(provincia);

        CentroVaccinaleDTO cvDTO = new CentroVaccinaleDTO(cv);

        CentroVaccinaleDTO cvSalvatoDTO = centroVaccinaleServiceImpl.insertCentro(cvDTO);

        assertEquals(cvSalvatoDTO.getId(),cv.getId());

        reset(centroVaccinaleRepository);
        reset(provinciaRepository);
    }

}
