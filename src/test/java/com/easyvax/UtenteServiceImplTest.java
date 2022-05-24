package com.easyvax;

import com.easyvax.dto.PersonaleDTO;
import com.easyvax.dto.UtenteDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.impl.PersonaleServiceImpl;
import com.easyvax.service.impl.UtenteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceImplTest implements PasswordEncoder {

    private UtenteServiceImpl utenteServiceImpl;


    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private ProvinciaRepository provinciaRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessageHelper helper;


    @BeforeEach
    void setUp() {
        utenteServiceImpl = new UtenteServiceImpl(utenteRepository, provinciaRepository, passwordEncoder, javaMailSender);
    }

    @Test
    void findByCF() {
        String cf = "RNCFNC030303E335M";
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();

        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale(cf).password("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).build();

        assertNotNull(utente);
        lenient().when(utenteRepository.existsByCodFiscale(cf)).thenReturn(true);
        lenient().when(utenteRepository.findByCodFiscale(cf)).thenReturn(utente);

        UtenteDTO utenteDTO = new UtenteDTO(utente);
        assertNotNull(utenteDTO);

        assertEquals(utenteDTO.getCodFiscale(), utenteServiceImpl.findByCF(cf).getCodFiscale());

        reset(utenteRepository);
    }

    @Test
    void deleteUtente() {
        Long id = 1L;

        lenient().when(utenteRepository.existsById(id)).thenReturn(true);
        Assertions.assertTrue(utenteServiceImpl.deleteUtente(id));
        verify(utenteRepository, atLeastOnce()).existsById(id);
        reset(utenteRepository);
    }

    @Test
    void insertUtente() {


        LocalDate dataNascita = LocalDate.ofEpochDay(2022 - 02 - 02);
        Regione regione = Regione.builder().id(7L).nome("Lazio").build();
        Provincia provincia = Provincia.builder().id(5l).nome("Roma").cap("00159").regione(regione).build();
        String password = "abcde";
        Utente utente = Utente.builder().id(1l).nome("Francesco").cognome("Ronca").codFiscale("test").email("test@test.test").ruolo(RoleEnum.ROLE_USER).provincia(provincia).dataNascita(dataNascita).password(password).build();

        UtenteDTO utenteDTO = new UtenteDTO(utente);
        assertNotNull(utenteDTO);


        lenient().when(provinciaRepository.existsById(utenteDTO.residenza)).thenReturn(true);
        lenient().when(provinciaRepository.findById(utenteDTO.residenza)).thenReturn(Optional.of(provincia));
        lenient().when(utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascita(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita())).thenReturn(false);
        lenient().when(utenteRepository.existsByEmail(utenteDTO.getEmail())).thenReturn(false);
        lenient().when(utenteRepository.existsByCodFiscale(utenteDTO.getCodFiscale())).thenReturn(false);

        String psw = utenteDTO.getPassword();
        String hashedPsw = this.encode(psw);


        lenient().when(passwordEncoder.encode(psw)).thenReturn(hashedPsw);

        lenient().when(utenteRepository.save(utente)).thenReturn(utente);

        assertEquals(utente.getCodFiscale(), utenteServiceImpl.insertUtente(utenteDTO).getCodFiscale());


        reset(utenteRepository);
        reset(provinciaRepository);
    }


    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
