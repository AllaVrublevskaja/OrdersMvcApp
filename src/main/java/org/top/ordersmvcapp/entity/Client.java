package org.top.ordersmvcapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// модель клиента
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_t")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_f", nullable = false)
    private String name;

    @Column(name = "email_f", nullable = false)
    private String email;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Order> orders;

    @Override
    public String toString() {
        return id + " - " + name + " - " + email;
    }
}
