package com.School_Auto.demo.controller;

import com.School_Auto.demo.entity.Message;
import com.School_Auto.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SSEController {

    private final MessageService messageService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public SSEController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/messages/{conversationId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessages(@PathVariable int conversationId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        // İlk mesajları gönder
        try {
            List<Message> messages = messageService.findAllByConversationId(conversationId);
            emitter.send(SseEmitter.event()
                    .name("messages")
                    .data((Object) messages));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void broadcastMessages(int conversationId) {
        try {
            List<Message> messages = messageService.findAllByConversationId(conversationId);
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("messages")
                            .data((Object) messages));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

