package com.easyvax.model;

import com.easyvax.dto.UtenteDTO;
import com.easyvax.exception.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String cognome;

    @NonNull
    private String codFiscale;

    private LocalDate dataNascita;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private String verificationCode;

    private boolean enabled;

    @NonNull
    @Enumerated(EnumType.STRING)
    private RoleEnum ruolo;


    @OneToMany(mappedBy="utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> somministrazioni = new ArrayList<>();

    @OneToMany(mappedBy="utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Personale> personale = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="id_residenza")
    public Provincia provincia;




    public Utente(UtenteDTO utenteDTO) {
        this.id = utenteDTO.id;
        this.nome = utenteDTO.nome;
        this.dataNascita=utenteDTO.getDataNascita();
        this.password= utenteDTO.getPassword();
        this.cognome = utenteDTO.cognome;
        this.email=utenteDTO.getEmail();
        this.verificationCode=utenteDTO.getVerificationCode();
        this.enabled=utenteDTO.isEnabled();
        this.ruolo=utenteDTO.getRuolo();
    }

    public String getNome_Cognome(){
        return this.getCognome().toUpperCase() + " "+ this.getNome().toUpperCase();
    }
}
