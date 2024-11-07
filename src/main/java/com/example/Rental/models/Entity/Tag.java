package com.example.Rental.models.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;



    public Tag(String tagName) {
        this.name=tagName;
    }





    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Item> items = new HashSet<>();

}
