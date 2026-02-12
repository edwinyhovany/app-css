// Entidad JPA usada por el adaptador de persistencia para mapear usuarios en la base de datos
package com.example.hexagonal_users_api.infraestructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    protected UserEntity() { }

    public UserEntity(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() { return id;}
    public String getName() { return name;}
    public String getEmail() { return email;}

    public void setId(Long id) { this.id = id;}
    public void setName(String name) { this.name = name;}
    public void setEmail(String email) { this.email = email;}
}
