package com.easyvax.model;


import com.easyvax.dto.RegioneDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Regione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @OneToMany(mappedBy="regione", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Provincia> province = new ArrayList<>();

    public Regione(RegioneDTO regioneDTO){
        this.id = regioneDTO.id;
        this.nome=regioneDTO.nome;
    }

}
