package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.dto.LoginRequestDto;
import com.rerelease.movie.rereleasemovie.dto.LoginResponseDto;
import com.rerelease.movie.rereleasemovie.dto.SignupRequestDto;
import com.rerelease.movie.rereleasemovie.dto.UserResponseDto;
import com.rerelease.movie.rereleasemovie.model.Users;
import com.rerelease.movie.rereleasemovie.repository.UserRepository;
import com.rerelease.movie.rereleasemovie.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 API
     *
     * @param request 회원가입 요청 DTO
     * @return 성공 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid SignupRequestDto request) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(request.getEmail())
                          .isPresent()) {
            return ResponseEntity.badRequest()
                                 .body("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화 후 저장
        Users newUser = Users.builder()
                             .email(request.getEmail())
                             .password(passwordEncoder.encode(request.getPassword()))
                             .nickname(request.getNickname())
                             .emailVerified(false)
                             .role(Users.Role.ROLE_USER)
                             .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("회원가입 완료 :)");
    }

    /**
     * 로그인 API
     *
     * @param request 로그인 요청 DTO (이메일, 비밀번호)
     * @return JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid LoginRequestDto request) {
        // 1. 사용자가 존재하는지 먼저 확인
        Users user = userRepository.findByEmail(request.getEmail())
                                   .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        try {
            // 2. 이메일 & 비밀번호 인증 수행
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(LoginResponseDto.builder()
                                                       .token(null)
                                                       .message("이메일 또는 비밀번호가 올바르지 않습니다.")
                                                       .build());
        }

        // 3. JWT 토큰 발급 후 JSON 형식으로 반환
        String token = jwtUtil.generateToken(request.getEmail());
        return ResponseEntity.ok(
                LoginResponseDto.builder()
                                .token(token)
                                .message("로그인 성공")
                                .build());
    }

    /**
     * 현재 로그인한 사용자 정보 조회 API
     *
     * @param userDetails 인증된 사용자 정보
     * @return 사용자 이메일
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // 사용자 정보 조회
        Users user = userRepository.findByEmail(userDetails.getUsername())
                                   .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // DTO 변환 후 반환
        UserResponseDto response = UserResponseDto.builder()
                                                  .email(user.getEmail())
                                                  .nickname(user.getNickname())
                                                  .build();

        return ResponseEntity.ok(response);
    }
}
