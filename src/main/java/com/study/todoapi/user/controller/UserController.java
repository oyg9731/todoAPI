package com.study.todoapi.user.controller;

import com.study.todoapi.auth.TokenUserInfo;
import com.study.todoapi.user.dto.request.LoginRequestDTO;
import com.study.todoapi.user.dto.request.UserSinUpRequestDTO;
import com.study.todoapi.user.dto.response.LoginResponseDTO;
import com.study.todoapi.user.dto.response.UserSinUpResponseDTO;
import com.study.todoapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    // 회운가입 요청처리
    @PostMapping
    public ResponseEntity<?> sinUP(
            @Validated @RequestPart("user") UserSinUpRequestDTO dto
            , @RequestPart("profileImage") MultipartFile profileImg
            ,BindingResult result){
        log.info("/api/auth POST! - {}", dto);

        if (profileImg != null) log.info("file-name: {}", profileImg);

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
        log.info("{}", email);
        return ResponseEntity.ok().body(flag);
    }

    // 로그인 요청처리
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @Validated @RequestBody LoginRequestDTO dto
    ) {
        try {
            LoginResponseDTO responseDTO = userService.authenticate(dto);
            log.info("login success!! by {}", responseDTO.getEmail());
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 일반회원을 프리미엄으로 상승시키는 요청 처리
    @PutMapping("/promote")
    // 그냥 이 권한을 가진 사람만 이 요청을 수행할수 있고
    // 이 권한이 아닌 유저는 강제로 403이 응답됨
    @PreAuthorize("hasRole('ROLE_COMMON')")
//    @PreAuthorize("hasRole('ROLE_COMMON') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> promote(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        log.info("/api/auth/promote PUT!");

        try {
            LoginResponseDTO responseDTO = userService.promoteToPremium(userInfo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
