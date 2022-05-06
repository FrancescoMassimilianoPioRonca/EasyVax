package com.easyvax.service.impl;

import com.easyvax.dto.PersonaleDTO;
import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.PersonaleEnum;
import com.easyvax.exception.enums.RichiestaEnum;
import com.easyvax.exception.enums.SomministrazioneEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.service.RichiestaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.FalseCondition;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RichiestaServiceImpl implements RichiestaService {

    private final OperatoreRepository operatoreRepository;
    private static RichiestaEnum richiestaEnum;
    private static CentroVaccinaleEnum centroVaccinaleEnum;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private final RichiestaRepository richiestaRepository;
    private final SomministrazioneRepository somministrazioneRepository;
    private final UtenteRepository utenteRepository;
    private JavaMailSender mailSender;


    /**
     * Con questo metodo, l'operatore, in base al suo id, riceve tutte le richieste generate dagli utenti non ancora smarcate, relative
     * al centro vaccinale in cui lavora
     *
     * @param idOperatore
     * @return List<RichiestaDTO>
     */
    @Override
    public List<RichiestaDTO> getRichiesteOperatore(Long idOperatore) {

        if (idOperatore != null && operatoreRepository.existsById(idOperatore)) {
            return richiestaRepository.getRichieste(idOperatore).stream().map(RichiestaDTO::new).collect(Collectors.toList());
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_NE");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    /**
     * Con questo metodo invece, l'utente può vedere tutte le richieste non ancora smarcate che ha eseguito
     *
     * @param idUtente
     * @return
     */
    @Override
    public List<RichiestaDTO> getRichiesteUtente(Long idUtente) {
        if (idUtente != null && utenteRepository.existsById(idUtente)) {
            return richiestaRepository.getRichiesteUtente(idUtente).stream().map(RichiestaDTO::new).collect(Collectors.toList());
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_NE");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    /**
     * Si occupa di accettare le richieste. Se approved è true, è stato autorizzato il cambio data
     * Se invece op1, op2 e quindi approved sono true allora è sato autrizzato il cambio sede
     * Viene inviata una email quando viene accettata ogni richiesta
     * Gli attributi op1 e op2 si riferiscono all'operatore della sede vecchia e op2 l'operatore della nuova sede. Entrambi devono essere a true per poter cambiare sede
     *
     * @param id
     */
    @Override
    public void accettaRichiesta(Long id) {

        if (id != null && richiestaRepository.existsById(id)) {
            Richiesta richiesta = richiestaRepository.findById(id).get();

            if (somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())) {

                Somministrazione somministrazione = somministrazioneRepository.findById(richiesta.getSomministrazione().getId()).get();

                if (richiesta.getNewData() != null && richiesta.getIdCentroVacc() == null) {
                    somministrazione.setDataSomministrazione(richiesta.getNewData());
                    somministrazione.setInAttesa(Boolean.FALSE);
                    richiesta.setApproved(Boolean.TRUE);
                    somministrazioneRepository.save(somministrazione);
                    richiestaRepository.save(richiesta);

                    try {
                        acceptEmail(richiesta.getId(), somministrazione);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (richiesta.getNewData() == null && richiesta.getIdCentroVacc() != null) {

                    if (richiesta.getApprovedOp1() != null && richiesta.getApprovedOp2() == null) {
                        richiesta.setApprovedOp2(true);
                        richiesta.setApproved(true);
                        somministrazione.setInAttesa(false);
                        richiestaRepository.save(richiesta);

                        try {
                            acceptEmail(richiesta.getId(), somministrazione);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if (centroVaccinaleRepository.existsById(richiesta.getIdCentroVacc())) {
                        CentroVaccinale cv = centroVaccinaleRepository.findById(richiesta.getIdCentroVacc()).get();
                        somministrazione.setCentro(cv);
                        richiesta.setApprovedOp1(true);
                        somministrazioneRepository.save(somministrazione);
                        richiestaRepository.save(richiesta);
                    } else {
                        centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
                        throw new ApiRequestException(centroVaccinaleEnum.getMessage());
                    }
                }
            }
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_NF");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }

    }

    /**
     * Rifiuto una richiesta
     *
     * @param id
     */
    @Override
    public void rifiutaRichiesta(Long id) {

        if (id != null && richiestaRepository.existsById(id)) {
            Richiesta richiesta = richiestaRepository.findById(id).get();

            richiesta.setApproved(Boolean.FALSE);

            richiestaRepository.save(richiesta);

            Somministrazione somministrazione = somministrazioneRepository.findById(richiesta.getSomministrazione().getId()).get();

            somministrazione.setInAttesa(Boolean.FALSE);

            somministrazioneRepository.save(somministrazione);

            try {
                rejectEmail(richiesta.getId(), somministrazione);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_NF");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    /**
     * Elimino una richiesta
     *
     * @param id
     * @return List<RichiestaDTO>
     */
    @Override
    public List<RichiestaDTO> deleteRichiesta(Long id) {

        if (richiestaRepository.existsById(id)) {
            richiestaRepository.deleteById(id);
            return richiestaRepository.findAll().stream().map(RichiestaDTO::new).collect(Collectors.toList());
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("R_NE");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    /**
     * Questo metodo gestisce sia le richieste di cambio data (generabile dagli utenti se per motivi validi devono per forza spostare la lor prenotazione anche
     * dopo il limite imposto di 2 giorni antecedenti) sia le richieste di cambio sede mantendendo la stessa prenotazione.
     * Abbiamo utilizzato una logica abbastanza semplice ma efficace ovvero: se la richiesta contiene l'attributo newData != null e i due flag op1 e op2 a null, allora vuol dire che
     * si intende cambiare la data. Se invece l'attributo newData ==null ma l'attributo centrovacc != null allora si intende cambiare la sede.
     * Tutto questo è poi gestito tramite una logica di flag approved (nel caso di cambio data) e op1 e op2 nel caso di cambio sede.
     * <p>
     * Appena si inserisce una richiestaviene poi inviata una email informativa con il codice della richiesta
     **/
    @Override
    public RichiestaDTO insertRichiesta(RichiestaDTO richiestaDTO) {

        if (somministrazioneRepository.existsById(richiestaDTO.idSomministrazione)) {
            Somministrazione somministrazione = somministrazioneRepository.findById(richiestaDTO.idSomministrazione).get();
            somministrazione.setInAttesa(Boolean.TRUE);

            Richiesta richiesta = new Richiesta(richiestaDTO);

            if (!(richiestaRepository.existsBySomministrazione_Id(somministrazione.getId()))) {

                if (richiestaDTO.getData() != null && richiestaDTO.getIdCentroVaccinale() == null) {
                    richiesta.setNewData(richiestaDTO.getData());
                    richiesta.setSomministrazione(somministrazione);

                    richiesta = richiestaRepository.save(richiesta);
                } else if (richiestaDTO.getData() == null && richiestaDTO.getIdCentroVaccinale() != null) {

                    if (centroVaccinaleRepository.existsById(richiestaDTO.IdCentroVaccinale)) {
                        richiesta.setSomministrazione(somministrazione);
                        richiesta.setIdCentroVacc(richiestaDTO.getIdCentroVaccinale());
                        richiesta = richiestaRepository.save(richiesta);
                    } else {
                        centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
                        throw new ApiRequestException(centroVaccinaleEnum.getMessage());
                    }
                }

                try {
                    sendEmail(richiesta.getId(), somministrazione);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return new RichiestaDTO(richiesta);
            } else {
                richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_E");
                throw new ApiRequestException(richiestaEnum.getMessage());
            }
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_E");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    /**
     * Invio l'email di inserimento richiesta
     *
     * @param id
     * @param somm
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */

    private void sendEmail(Long id, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

        Utente utente = utenteRepository.findById(somm.getUtente().getId()).get();
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Inserimento richiesta";
        String content = "Caro [[name]],<br>"
                + "hai appena inserito una richiesta (n. [[nr_richiesta]]) per posticipare la data della tua vaccinazione.<br>"
                + "Ti ricordiamo che non potrai eseguire altre operazioni sulla tua prenotazione <b>(cod : [[codice]])</b , fino a quando non riceverai l'esito della richiesta.<br>"
                + "Non appena il personale accetterà o rifiuterà la tua richiesta ti invieremo una email.<br>"
                + "Grazie per averci scelto.<br>"
                + " Saluti,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", utente.getNome_Cognome());
        content = content.replace("[[nr_richiesta]]", id.toString());
        content = content.replace("[[codice]]", somm.getCodiceSomm());


        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * Invio l'email per avvenuta accettazione della richiesta
     *
     * @param id
     * @param somm
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private void acceptEmail(Long id, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

        Utente utente = utenteRepository.findById(somm.getUtente().getId()).get();
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Esito richiesta";
        String content = "Caro [[name]],<br>"
                + "La tua richiesta <b>(cod : [[codice]])</b è stata accettata.<br>"
                + "Puoi nuovamente verificare i dettagli della prenotazione nella tua area personale.<br>"
                + "Grazie per averci scelto.<br>"
                + " Saluti,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", utente.getNome_Cognome());
        content = content.replace("[[nr_richiesta]]", id.toString());
        content = content.replace("[[codice]]", somm.getCodiceSomm());


        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * Invio l'email per rifiuto richiesta
     *
     * @param id
     * @param somm
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private void rejectEmail(Long id, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

        Utente utente = utenteRepository.findById(somm.getUtente().getId()).get();
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Esito richiesta";
        String content = "Caro [[name]],<br>"
                + "La tua richiesta <b>(cod : [[codice]])</b  è stata respinta.<br>"
                + "Puoi nuovamente visualizzare e modificare la tua prenotazione nella tua area personale <br>"
                + "Grazie per averci scelto.<br>"
                + " Saluti,<br>"
                + "EasyVax.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", utente.getNome_Cognome());
        content = content.replace("[[nr_richiesta]]", id.toString());
        content = content.replace("[[codice]]", somm.getCodiceSomm());


        helper.setText(content, true);

        mailSender.send(message);
    }
}
