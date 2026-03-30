package com.uf.assistance.event;

import com.uf.assistance.domain.ai.AISubscription;
import com.uf.assistance.domain.chat.Chat;
import com.uf.assistance.domain.chat.MessageType;
import com.uf.assistance.domain.user.User;
import com.uf.assistance.service.AISubscriptionService;
import com.uf.assistance.service.UserService;
import com.uf.assistance.util.CustomUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private final AISubscriptionService aiSubscriptionService;
    private final UserService userService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String aiSubscriptionIdStr = (String) headerAccessor.getSessionAttributes().get("aiSubscriptionId");
        Long userId = CustomUserUtil.getCurrentUserId();
        String userLoginId = CustomUserUtil.getCurrentUserLoginId();

        if (userId != null && aiSubscriptionIdStr != null) {
            Long aiSubscriptionId = Long.parseLong(aiSubscriptionIdStr);
            AISubscription aiSubscription = aiSubscriptionService.getAISubScriptionById(aiSubscriptionId);
            User user = userService.findUserEntityById(userId);

            Chat chat = Chat.builder()
                    .type(MessageType.LEAVE)
                    .sender(user)
                    .content(userLoginId + "님이 퇴장하셨습니다.")
                    .aiSubscription(aiSubscription)
                    .build();
            messagingTemplate.convertAndSend("/topic/public/" + aiSubscriptionId, chat);
        }
    }
}
