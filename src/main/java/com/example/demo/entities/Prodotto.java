package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "prodotto", schema = "dbServer")
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 70)
    private String barCode;

    @Column(nullable = true, length = 500)
    private String descrizione;

    @Column(nullable = false, length = 50)
    private float prezzo;

    @Column(nullable = false)
    private int quantita;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String type;

    @Version
    @Column(nullable = false)
    @JsonIgnore
    private long version;

    @OneToMany(targetEntity = ProdottoInAcquisto.class, mappedBy = "prodotto")
    @JsonIgnore
    @ToString.Exclude
    private List<ProdottoInAcquisto> prodottoInAcquisti; //lista degli Acquisti in cui è contenuto questo prodotto
}

/*
types: -- 1: SHOES, 2: CONSOLE, 3: CAMERA, 4: PC, 5: SMARTPHONE, 6: HEADPHONE, 7: WATCH, 8: KEYBOARD
 */
