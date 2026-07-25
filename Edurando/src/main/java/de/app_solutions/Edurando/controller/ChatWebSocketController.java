package de.app_solutions.Edurando.controller;

import de.app_solutions.Edurando.model.ChatMessage;
import de.app_solutions.Edurando.model.ChatMessageRequest;
import de.app_solutions.Edurando.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request) {
        ChatMessage saved = chatService.saveMessage(request);
        // Deliver to both receiver and sender so both update in real time
        messagingTemplate.convertAndSend("/topic/messages/" + request.receiver(), saved);
        messagingTemplate.convertAndSend("/topic/messages/" + request.sender(), saved);
    }
}
