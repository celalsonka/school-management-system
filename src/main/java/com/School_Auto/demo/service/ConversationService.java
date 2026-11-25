package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Conversation;

import java.util.List;

public interface ConversationService {
    List<Conversation> findAll();

    Conversation findById(int theId);
    
    Conversation findById1AndAd2(int theId1, int theId2);
    
    Conversation save(Conversation theConversation);

    void deleteById(int theId);
}
