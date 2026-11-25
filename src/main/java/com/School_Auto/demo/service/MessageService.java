package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> findAll();

    Message findById(int id);

    Message save(Message message);

    void deleteById(int id);

    List<Message> findAllByConversationId(int conversationId);
}
