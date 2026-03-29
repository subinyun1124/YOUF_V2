package com.uf.assistance.web;

import com.uf.assistance.config.jwt.CustomUserDetails;
import com.uf.assistance.dto.ResponseDto;
import com.uf.assistance.dto.ai.AISubScriptionReqDto;
import com.uf.assistance.dto.ai.AISubScriptionRespDto;
import com.uf.assistance.dto.ai.CustomAIRespDto;
import com.uf.assistance.service.AISubscriptionService;
import com.uf.assistance.util.CustomDateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/aisubscription")
@Tag(name = "AI 구독", description = "AI 구독 CRUD 관련 API")
public class AISubscriptionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AISubscriptionService aiSubscriptionService;

    @GetMapping("")
    @Operation(summary = "사용자 ID 기준으로 CustomAI 구독 목록 가져오기")
    public ResponseEntity<ResponseDto<List<CustomAIRespDto>>> getSubscribedCustomAIsbyUserId(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            List<CustomAIRespDto> customAIRespDtos = aiSubscriptionService.getSubscribedCustomAIs(userId);
            return new ResponseEntity<>(new ResponseDto<>(1, "사용자 구독리스트 조회", CustomDateUtil.toStringFormat(LocalDateTime.now()), customAIRespDtos), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("사용자 구독리스트 조회 실패: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseDto<>(-1, "사용자 구독리스트 조회 실패", CustomDateUtil.toStringFormat(LocalDateTime.now()), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/relation/")
    @Operation(summary = "사용자 ID 기준으로 구독정보 가져오기")
    public ResponseEntity<ResponseDto<List<AISubScriptionRespDto>>> getSubscriptionsbyUserId(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            List<AISubScriptionRespDto> customAIRespDtos = aiSubscriptionService.getSubscriptions(userId);
            return new ResponseEntity<>(new ResponseDto<>(1, "사용자 구독리스트 조회", CustomDateUtil.toStringFormat(LocalDateTime.now()), customAIRespDtos), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("사용자 구독리스트 조회 실패: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseDto<>(-1, "사용자 구독리스트 조회 실패", CustomDateUtil.toStringFormat(LocalDateTime.now()), null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/subscribe")
    @Operation(summary = "구독정보 등록")
    public ResponseEntity<ResponseDto<AISubScriptionRespDto>> subscribe(Authentication authentication, @RequestBody AISubScriptionReqDto dto) {

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            AISubScriptionRespDto aiSubScriptionRespDto = aiSubscriptionService.subscribe(userId, dto.getCustomAiId());

            return new ResponseEntity<>(new ResponseDto<>(1, "AI 구독성공 -" + userDetails.getUserId(), CustomDateUtil.toStringFormat(LocalDateTime.now()), aiSubScriptionRespDto), HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(-1, "AI 구독실패 " + e.getMessage(), CustomDateUtil.toStringFormat(LocalDateTime.now()), null), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{aisubscriptionId}")
    @Operation(summary = "구독정보 삭제")
    public ResponseEntity<?> unsubscribe(@PathVariable Long aisubscriptionId) {
        HashMap<String, String> rtnMap = (HashMap<String, String>) aiSubscriptionService.unsubscribe(aisubscriptionId);
        return new ResponseEntity<>(new ResponseDto<>(Integer.valueOf(rtnMap.get("code")), "AI 구독취소", CustomDateUtil.toStringFormat(LocalDateTime.now()), rtnMap.get("msg")), HttpStatus.OK);
    }

    @Deprecated
    @DeleteMapping(value = "/delete/{customAiId}/{userId}")
    @Operation(summary = "구독정보 삭제")
    public ResponseEntity<ResponseDto<String>> unsubscribeCustomAI(@PathVariable Long customAiId, @PathVariable Long userId) {
        HashMap<String, String> rtnMap = (HashMap<String, String>) aiSubscriptionService.unsubscribe(userId, customAiId);
        return new ResponseEntity<>(new ResponseDto<>(Integer.valueOf(rtnMap.get("code")), "AI 구독취소", CustomDateUtil.toStringFormat(LocalDateTime.now()), rtnMap.get("msg")), HttpStatus.OK);
    }
}
