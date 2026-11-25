package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ConversationDao;
import com.School_Auto.demo.entity.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationDao conversationDao;

    @Autowired
    public ConversationServiceImpl(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }

    @Override
    public List<Conversation> findAll() {
        return conversationDao.findAll();
    }

    @Override
    public Conversation findById(int theId) {
        return conversationDao.findById(theId);
    }

    @Override
    public Conversation findById1AndAd2(int theId1, int theId2) {
        return conversationDao.findById1AndAd2(theId1, theId2);
    }

    @Override
    public Conversation save(Conversation theConversation) {
        return conversationDao.save(theConversation);
    }

    @Override
    public void deleteById(int theId) {
        conversationDao.deleteById(theId);
    }
}
