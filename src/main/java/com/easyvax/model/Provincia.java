package com.easyvax.model;


import com.easyvax.DTO.ProvinciaDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String cap;

    @ManyToOne
    @JoinColumn(name="id_regione")
    private Regione regione;

    @OneToMany(mappedBy="provincia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CentroVaccinale> centriVaccinali = new ArrayList<>();

    @OneToMany(mappedBy="provincia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Utente> utenti = new ArrayList<>();



    public Provincia (ProvinciaDTO provincia){
        this.id = provincia.getId();
        this.nome=provincia.getNome();
        this.cap= provincia.getCap();
    }
}
