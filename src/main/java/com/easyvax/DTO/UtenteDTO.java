package com.easyvax.DTO;

import com.easyvax.exception.enums.RoleEnum;
import com.easyvax.model.Utente;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtenteDTO {

    public Long id;
    public String nome;
    public String cognome;
    private String codFiscale;
    private LocalDate dataNascita;
    private String password;
    private String email;
    private String verificationCode;
    private boolean enabled;

    public Long residenza;
    public RoleEnum ruolo;


    public UtenteDTO(Utente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.dataNascita=utente.getDataNascita();
        this.ruolo=utente.getRuolo();
        this.codFiscale=utente.getCodFiscale();
        this.password=utente.getPassword();
        this.residenza=utente.getProvincia().getId();
        this.verificationCode=utente.getVerificationCode();
        this.enabled=utente.isEnabled();
        this.email=utente.getEmail();
    }

}
