package com.study.todoapi.user.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter@Getter@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; // account가 아니라 랜덤식별번호

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @CreationTimestamp
    private LocalDateTime joinDate;
}
