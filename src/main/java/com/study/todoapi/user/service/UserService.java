package com.study.todoapi.user.service;

import com.study.todoapi.user.dto.request.UserSinUpRequestDTO;
import com.study.todoapi.user.dto.response.UserSinUpResponseDTO;
import com.study.todoapi.user.entity.User;
import com.study.todoapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 처리
    public UserSinUpResponseDTO create(UserSinUpRequestDTO dto){

        if (dto == null){
            throw new RuntimeException("회원가입 입력정보가 없습니다!");
        }
        String email = dto.getEmail();

        if (userRepository.existsByEmail(email)){
            log.warn("이메일이 중복되었습니다!! - {}", email);
            throw new RuntimeException("중복된 이메일입니다!!");
        }
        User saved = userRepository.save(dto.toEntity(passwordEncoder));

        log.info("회원가입 성공!! - {}", saved);

        return new UserSinUpResponseDTO(saved); // 회원가입 정보를 클라이언트에게 리턴

    }
}