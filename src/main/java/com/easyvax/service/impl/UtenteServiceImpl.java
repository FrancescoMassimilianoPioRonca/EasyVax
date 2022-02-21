package com.easyvax.service.impl;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.ProvinciaDTO;
import com.easyvax.DTO.UtenteDTO;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.ProvinciaEnum;
import com.easyvax.exception.enums.UtenteEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Utente;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.UtenteRepository;
import com.easyvax.service.service.UtenteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Service
@Slf4j
@AllArgsConstructor

public class UtenteServiceImpl  implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final ProvinciaRepository provinciaRepository;
    private static UtenteEnum utenteEnum;
    private JavaMailSender mailSender;

    @Override
    public UtenteDTO insertUtente(UtenteDTO utenteDTO){
        
        String siteUrl= "http://localhost:8080";

        if(!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascitaAndRuolo(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita(), utenteDTO.getRuolo()) && !utenteRepository.existsByEmail(utenteDTO.getEmail()) && !utenteRepository.existsByCodFiscale(utenteDTO.getCodFiscale())){;
            Utente utente = new Utente(utenteDTO);
            Provincia provincia = provinciaRepository.findById(utenteDTO.residenza).get();
            utente.setNome(utenteDTO.nome);
            utente.setCognome(utenteDTO.cognome);
            utente.setCodFiscale(utenteDTO.getCodFiscale());
            utente.setDataNascita(utenteDTO.getDataNascita());
            utente.setPassword(utenteDTO.getPassword());
            utente.setRuolo(utente.getRuolo());
            utente.setProvincia(provincia);
            utente.setEnabled(false);
            
            String randomCode = RandomString.make(64);
            
            utente.setVerificationCode(randomCode);


            utente = utenteRepository.save(utente);

            try {
                sendVerificationEmail(utente,siteUrl);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return new UtenteDTO(utente);
        }
        else{
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    private void sendVerificationEmail (Utente utente, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]",utente.getNome_Cognome());
        String verifyURL = siteUrl + "/verify?code=" + utente.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public UtenteDTO getDetails(Long id) {
        if(id!=null && utenteRepository.existsById(id)){
            return new UtenteDTO(utenteRepository.findById(id).get());
        }
        else{
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    @Override
    public List<UtenteDTO> updateAnagrafica(UtenteDTO utenteDTO) {

        if (utenteRepository.existsById(utenteDTO.id)) {
            Provincia provincia = provinciaRepository.findById(utenteDTO.getResidenza()).get();

            if(!utenteRepository.existsByNomeAndCognomeAndCodFiscaleAndDataNascitaAndRuolo(utenteDTO.getNome(), utenteDTO.getCognome(), utenteDTO.getCodFiscale(), utenteDTO.getDataNascita(),utenteDTO.getRuolo()) && provinciaRepository.existsById(utenteDTO.getResidenza())) {

                Utente utente = new Utente(utenteDTO);

                utente.setNome(utenteDTO.getNome());
                utente.setCodFiscale(utenteDTO.getCognome());
                utente.setDataNascita(utenteDTO.getDataNascita());
                utente.setPassword(utenteDTO.getPassword());
                utente.setCognome(utenteDTO.getCognome());
                utente.setProvincia(provincia);
                if(utenteDTO.getRuolo().equals("AMMINISTRATORE")){
                    utente.setRuolo(utenteDTO.getRuolo());
                }
            }
            else{
                utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_AE");
                throw new ApiRequestException(utenteEnum.getMessage());
            }
        }
        else
        {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }

        return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<UtenteDTO> findAll() {
        if(!utenteRepository.findAll().isEmpty())
            return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
        else{
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
    @Override
    public List<UtenteDTO> deleteUtente(Long id) {
        if(utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return utenteRepository.findAll().stream().map(UtenteDTO::new).collect(Collectors.toList());
        }
        else {
            utenteEnum = UtenteEnum.getUtenteEnumByMessageCode("UTE_NF");
            throw new ApiRequestException(utenteEnum.getMessage());
        }
    }

    /*@Override
    public List<UtenteDTO> findByRuolo(Boolean personale) {
        return null;
    }*/
}
