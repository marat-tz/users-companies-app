package com.usercompanies.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Long budget;

}


//     @ManyToOne
//    @JoinColumn(name = "user_id")
//    User user;

// @JoinTable(name = "compilations_events",
//            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
//    @ManyToMany
//    List<Event> events;