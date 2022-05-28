package com.easyvax.service.impl;

import com.easyvax.dto.RichiestaDTO;
import com.easyvax.exception.enums.CentroVaccinaleEnum;
import com.easyvax.exception.enums.RichiestaEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.*;
import com.easyvax.repository.*;
import com.easyvax.service.service.RichiestaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            Operatore operatore = operatoreRepository.findById(idOperatore).get();
            CentroVaccinale cv = operatore.getCentroVaccinale();
            return richiestaRepository.getRichieste(cv.getId()).stream().map(RichiestaDTO::new).collect(Collectors.toList());
        } else {
            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_NE");
            throw new ApiRequestException(richiestaEnum.getMessage());
        }
    }

    @Override
    public List<RichiestaDTO> findAll() {
        if (!richiestaRepository.findAll().isEmpty())
            return richiestaRepository.findAll().stream().map(RichiestaDTO::new).collect(Collectors.toList());
        else {
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
     * @param idRichiesta,idOperatore
     */
    @Override
    public boolean accettaRichiesta(Long idRichiesta, Long idOperatore) {

        if (idRichiesta != null && richiestaRepository.existsById(idRichiesta) && operatoreRepository.existsById(idOperatore)) {
            Richiesta richiesta = richiestaRepository.findById(idRichiesta).get();

            if (somministrazioneRepository.existsById(richiesta.getSomministrazione().getId())) {

                Somministrazione somministrazione = somministrazioneRepository.findById(richiesta.getSomministrazione().getId()).get();

                if (richiesta.getNewData() != null && richiesta.getNewCentro() == null) {
                    if(richiesta.getIdOp1()!=idOperatore && (operatoreRepository.checkOperatore(somministrazione.getCentro().getId(),idOperatore)!=0)) {
                        somministrazione.setDataSomministrazione(richiesta.getNewData());
                        somministrazione.setInAttesa(Boolean.FALSE);
                        richiesta.setApproved(Boolean.TRUE);
                        richiesta.setIdOp1(idOperatore);
                        somministrazioneRepository.save(somministrazione);
                        richiestaRepository.save(richiesta);
                        return true;
                    }
                    else{
                        richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_AA");
                        throw new ApiRequestException(richiestaEnum.getMessage());
                    }

                } else if (richiesta.getNewData() == null && richiesta.getNewCentro() != null) {

                    if (richiesta.getApprovedOp1() != null && richiesta.getApprovedOp2() == null) {
                        if(richiesta.getIdOp2()!=idOperatore && (operatoreRepository.checkOperatore(richiesta.getNewCentro(),idOperatore))!=0) {
                            richiesta.setApprovedOp2(true);
                            richiesta.setIdOp2(idOperatore);
                            CentroVaccinale cv = centroVaccinaleRepository.findById(richiesta.getNewCentro()).get();
                            somministrazione.setCentro(cv);
                            richiesta.setApproved(true);
                            somministrazione.setInAttesa(false);
                            somministrazioneRepository.save(somministrazione);
                            richiestaRepository.save(richiesta);
                            return true;
                        }
                        else{
                            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_AA");
                            throw new ApiRequestException(richiestaEnum.getMessage());
                        }
                    }
                    if (centroVaccinaleRepository.existsById(richiesta.getNewCentro())) {
                        if(richiesta.getIdOp1()==null && (operatoreRepository.checkOperatore(somministrazione.getCentro().getId(),idOperatore))!=0) {
                            richiesta.setApprovedOp1(true);
                            richiesta.setIdOp1(idOperatore);
                            somministrazioneRepository.save(somministrazione);
                            richiestaRepository.save(richiesta);
                            return true;
                        }
                        else{
                            richiestaEnum = RichiestaEnum.getRichiestEnumByMessageCode("RS_AA");
                            throw new ApiRequestException(richiestaEnum.getMessage());
                        }
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
        return false;
    }

    /**
     * Rifiuto una richiesta
     *
     * @param id
     */
    @Override
    public boolean rifiutaRichiesta(Long id) {

        if (id != null && richiestaRepository.existsById(id)) {
            Richiesta richiesta = richiestaRepository.findById(id).get();

            richiesta.setApproved(Boolean.FALSE);

            richiestaRepository.save(richiesta);

            Somministrazione somministrazione = somministrazioneRepository.findById(richiesta.getSomministrazione().getId()).get();

            somministrazione.setInAttesa(Boolean.FALSE);

            somministrazioneRepository.save(somministrazione);

            return true;

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

            if ((richiestaRepository.findBySomministrazione_IdAndApproved(somministrazione.getId())) == 0) {

                if (richiestaDTO.getData() != null && richiestaDTO.getIdNewcentro() == null) {
                    richiesta.setNewData(richiestaDTO.getData());
                    richiesta.setSomministrazione(somministrazione);
                    CentroVaccinale cv = centroVaccinaleRepository.findById(somministrazione.getCentro().getId()).get();
                    richiesta.setOldCentroVacc(cv);
                    richiestaRepository.save(richiesta);
                } else if (richiestaDTO.getData() == null && richiestaDTO.getIdNewcentro() != null) {

                    if (centroVaccinaleRepository.existsById(richiestaDTO.getIdNewcentro())) {
                        richiesta.setSomministrazione(somministrazione);
                        CentroVaccinale old = centroVaccinaleRepository.findById(somministrazione.getCentro().getId()).get();
                        richiesta.setOldCentroVacc(old);
                        richiesta.setNewCentro(richiestaDTO.getIdNewcentro());
                        richiestaRepository.save(richiesta);
                    } else {
                        centroVaccinaleEnum = CentroVaccinaleEnum.getCentroVaccinaleEnumByMessageCode("CV_NF");
                        throw new ApiRequestException(centroVaccinaleEnum.getMessage());
                    }
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
     * @param code
     * @param somm
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */

    public void sendEmail(String code, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

        Utente utente = utenteRepository.findById(somm.getUtente().getId()).get();
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Inserimento richiesta";
        String content = "Caro [[name]],<br>"
                + "hai appena inserito una richiesta relativa alla somministrazione (cod. [[nr_richiesta]]) per posticipare la data della tua vaccinazione.<br>"
                + "Ti ricordiamo che non potrai eseguire altre operazioni sulla tua prenotazione, fino a quando non riceverai l'esito della richiesta.<br>"
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
        content = content.replace("[[nr_richiesta]]", code);
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
    public void acceptEmail(Long id, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

        Utente utente = utenteRepository.findById(somm.getUtente().getId()).get();
        String toAddress = utente.getEmail();
        String fromAddress = "easyVaxNOREPLY@gmail.com";
        String senderName = "EasyVax";
        String subject = "Esito richiesta";
        String content = "Caro [[name]],<br>"
                + "La tua richiesta <b>(cod : [[codice]])</b> è stata accettata.<br>"
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
    public void rejectEmail(Long id, Somministrazione somm) throws MessagingException, UnsupportedEncodingException {

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
