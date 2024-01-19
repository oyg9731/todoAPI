package com.study.todoapi.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.todoapi.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSinUpResponseDTO {

    private String email;
    private String userName;

    @JsonProperty("join-date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    public UserSinUpResponseDTO(User user){
        this.email = user.getEmail();
        this.userName = user.getUsername();
        this.joinDate = user.getJoinDate();

    }
}
