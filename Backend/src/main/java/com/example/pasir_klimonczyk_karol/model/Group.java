package com.example.pasir_klimonczyk_karol.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`groups`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // Nazwa grupy

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // Właściciel grupy (może zapraszać, usuwać)

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;   // Lista członków w grupie
}
