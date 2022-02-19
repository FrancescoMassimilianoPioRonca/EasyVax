package com.easyvax.model;

import com.easyvax.DTO.UtenteDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @NonNull
    private LocalDate dataNascita;

    @NonNull
    private String ruolo;

    @NonNull
    private String password;



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
        this.ruolo=utenteDTO.getRuolo();

    }
}
