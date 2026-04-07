package com.uf.assistance.event;

import com.uf.assistance.domain.ai.AISubscription;
import com.uf.assistance.domain.chat.Chat;
import com.uf.assistance.domain.chat.MessageType;
import com.uf.assistance.domain.user.User;
import com.uf.assistance.service.AISubscriptionService;
import com.uf.assistance.service.UserService;
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
        Object userIdObj = headerAccessor.getSessionAttributes().get("userId");
        Object subscriptionIdObj = headerAccessor.getSessionAttributes().get("subscriptionId");

        if (userIdObj == null || subscriptionIdObj == null) {
            return;
        }

        Long userId = Long.parseLong(userIdObj.toString());
        Long aiSubscriptionId = Long.parseLong(subscriptionIdObj.toString());
        AISubscription aiSubscription = aiSubscriptionService.getAISubScriptionById(aiSubscriptionId);
        User user = userService.findUserEntityById(userId);

        Chat chat = Chat.builder()
                .type(MessageType.LEAVE)
                .sender(user)
                .content(user.getUsername() + "님이 퇴장하셨습니다.")
                .aiSubscription(aiSubscription)
                .build();
        messagingTemplate.convertAndSend("/topic/public/ai/" + aiSubscriptionId, chat);
    }
}
