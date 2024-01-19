package com.study.todoapi.user.controller;

import com.study.todoapi.user.dto.request.UserSinUpRequestDTO;
import com.study.todoapi.user.dto.response.UserSinUpResponseDTO;
import com.study.todoapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 회운가입 요청처리
    @PostMapping
    public ResponseEntity<?> sinUP(
            @Validated @RequestBody UserSinUpRequestDTO dto,
            BindingResult result
                                   ){
        log.info("/api/auth POST! - {}", dto);

        if (result.hasErrors())
        {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        try {
            UserSinUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 이메일 중복 확인 요청처리
    @GetMapping("/check")
    public ResponseEntity<?> check(String email){
        boolean flag = userService.isDuplicateEmail(email);
        log.debug("{} 중복?? - {}", email, flag);

        return ResponseEntity.ok().body(flag);
    }
}