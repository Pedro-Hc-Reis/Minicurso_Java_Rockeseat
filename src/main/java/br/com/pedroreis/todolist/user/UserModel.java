package br.com.pedroreis.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity ( name = "tb_users" )
public class UserModel {

    @Id
    @GeneratedValue ( generator = "UUID" )
    private UUID id;

    @Column ( unique = true )
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;
}
