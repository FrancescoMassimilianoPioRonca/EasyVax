package com.easyvax.service.impl;

import com.easyvax.dto.UtenteDTO;
import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.exception.enums.UtenteEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.service.UtenteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.mail.javamail.JavaMailSender;


@Service
@Slf4j
@AllArgsConstructor

public class UtenteServiceImpl implements UtenteService, UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final ProvinciaRepository provinciaRepository;
    private final PasswordEncoder passwordEncoder;
    private static UtenteEnum utenteEnum;
    private JavaMailSender mailSender;


    /**
     * Ricerco l'username per security
     *
     * @param cf
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String cf) throws UsernameNotFoundException {

        if (cf == null || !utenteRepository.existsByCodFiscale(cf.toUpperCase(Locale.ROOT))) {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
        Utente utente = utenteRepository.findByCodFiscale(cf.toUpperCase(Locale.ROOT));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(utente.getRuolo().toString()));
        return new org.springframework.security.core.userdetails.User(utente.getCodFiscale(), utente.getPassword(), authorities);
    }


    /**
     * Inserisco un nuovo utente che di base ha ruolo USER ed invio mail per la verifica
     *
     * @param utenteDTO
     * @return UtenteDTO
     */
    @Override
    public UtenteDTO insertUtente(UtenteDTO utenteDTO) {

        String siteUrl = "http://localhost:8080";

        if (provinciaRepository.existsById(utenteDTO.getResidenza())) {
            Provincia provincia = provinciaRepository.findById(utenteDTO.residenza).get();

            if (!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascita(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita()) && !utenteRepository.existsByEmail(utenteDTO.getEmail()) && !utenteRepository.existsByCodFiscale(utenteDTO.getCodFiscale())) {  //campi null

                Utente utente = new Utente(utenteDTO);

                utente.setNome(utenteDTO.nome);
                utente.setCognome(utenteDTO.cognome);

                utente.setCodFiscale(utenteDTO.getCodFiscale().toUpperCase(Locale.ROOT));

                utente.setDataNascita(utenteDTO.getDataNascita());
                utente.setPassword(passwordEncoder.encode(utenteDTO.getPassword()));
                utente.setEmail(utenteDTO.getEmail());

                utente.setProvincia(provincia);

                utente.setRuolo(RoleEnum.ROLE_USER);

                //Codice per la verifica via email
                //utente.setEnabled(false);


                utenteRepository.save(utente);

                return new UtenteDTO(utente);
            } else {
                utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
                throw new ApiRequestException(utenteEnum.getMessage());
            }
        } else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_PE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }


    /**
     * Inserisco un utente admin e invio mail per la verifica
     *
     * @param utenteDTO
     * @return UtenteDTO
     */
    @Override
    public UtenteDTO insertAdminUtente(UtenteDTO utenteDTO) {


        if (!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascita(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita()) && !utenteRepository.existsByEmail(utenteDTO.getEmail()) && !utenteRepository.existsByCodFiscale(utenteDTO.getCodFiscale())) {

            Utente utente = new Utente(utenteDTO);
            Provincia provincia = provinciaRepository.findById(utenteDTO.residenza).get();


            utente.setNome(utenteDTO.nome);
            utente.setCognome(utenteDTO.cognome);
            utente.setCodFiscale(utenteDTO.getCodFiscale().toUpperCase(Locale.ROOT));
            utente.setDataNascita(utenteDTO.getDataNascita());
            utente.setPassword(passwordEncoder.encode(utenteDTO.getPassword()));

            utente.setEmail(utenteDTO.getEmail());
            utente.setRuolo(RoleEnum.ROLE_ADMIN);

            utente.setProvincia(provincia);

            /*String randomCode = RandomString.make(64);

            utente.setVerificationCode(randomCode);*/

            utente.setEnabled(false);

            utenteRepository.save(utente);

            return new UtenteDTO(utente);
        } else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    /**
     * Invio mail per la verifica
     *
     * @param utente
     * @param siteUrl
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendVerificationEmail(Utente utente, String siteUrl) throws MessagingException, UnsupportedEncodingException {

        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Completa la registrazione";
        String content = "Gentile [[name]],<br>"
                + "Per favore, clicca sul link in basso per completare il processo di registrazione:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Grazie,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);


        content = content.replace("[[name]]", utente.getNome_Cognome());
       //String verifyURL = siteUrl + "/verify?code=" + utente.getVerificationCode();

       // content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * Ricevo i dettagli di un utente
     *
     * @param id
     * @return UtenteDTO
     */
    @Override
    public UtenteDTO getDetails(Long id) {
        if (id != null && utenteRepository.existsById(id)) {
            return new UtenteDTO(utenteRepository.findById(id).get());
        } else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    /**
     * Modifico l'anagrafica di un utente
     *
     * @param utenteDTO
     * @return List<UtenteDTO>
     */
    @Override
    public List<UtenteDTO> updateAnagrafica(UtenteDTO utenteDTO) {

        if (utenteRepository.existsById(utenteDTO.id)) {
            if (!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascita(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita()) && provinciaRepository.existsById(utenteDTO.getResidenza())) {

                Utente utente = new Utente(utenteDTO);

                utente.setNome(utenteDTO.nome);
                utente.setCognome(utenteDTO.cognome);
                utente.setCodFiscale(utenteDTO.getCodFiscale());
                utente.setDataNascita(utenteDTO.getDataNascita());
                utente.setPassword(passwordEncoder.encode(utenteDTO.getPassword()));
                utente.setEmail(utenteDTO.getEmail());

                utenteRepository.save(utente);

            } else {
                utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
                throw new ApiRequestException(utenteEnum.getMessage());
            }
        } else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }

        return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
    }


    /**
     * Cerco tutti gli uenti
     *
     * @return List<UtenteDTO>
     */
    @Override
    public List<UtenteDTO> findAll() {
        if (!utenteRepository.findAll().isEmpty())
            return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTI_NE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    /* @Override
     public List<UtenteDTO> finByCap(String cap) {
         if(cap != null && (provinciaRepository.existsByCap(cap)))
             return utenteRepository.findByCap(cap).stream().map(UtenteDTO::new).collect(Collectors.toList());
         else {
             utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_CAP_NF");
             throw new ApiRequestException(utenteEnum.getMessage());
         }
     }

     @Override
     public List<UtenteDTO> findByCognome(String cognome) {
         if(cognome != null && (utenteRepository.existsByCognome(cognome)))
             return utenteRepository.findByCognome(cognome).stream().map(UtenteDTO::new).collect(Collectors.toList());
         else {
             utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
             throw new ApiRequestException(utenteEnum.getMessage());
         }
     }
 */

    /**
     * Elimino un utente se esiste, successivamente elimino e restituisco true. Altrimenti genero eccezione custom per il front-end
     *
     * @param id
     * @return List<UtenteDTO>
     */
    @Override
    public Boolean deleteUtente(Long id) {
        if (utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return true;
        } else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }


    /**
     * Cerco l'utente in base al codice fiscale
     *
     * @param cf
     * @return UtenteDTO
     */
    @Override
    public UtenteDTO findByCF(String cf) {
        if (cf != null && utenteRepository.existsByCodFiscale(cf.toUpperCase(Locale.ROOT)))
            return new UtenteDTO(utenteRepository.findByCodFiscale(cf.toUpperCase(Locale.ROOT)));
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

}
