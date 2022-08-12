package com.interview.bci.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`user`")
public class User {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private boolean isActive;

    private String name;

    private String email;

    private String password;

    @OneToMany(mappedBy="number")
    private List<Phone> phones;

}
