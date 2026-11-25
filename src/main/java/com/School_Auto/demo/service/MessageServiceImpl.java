package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.MessageDao;
import com.School_Auto.demo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> findAll() {
        return messageDao.findAll();
    }

    @Override
    public Message findById(int id) {
        return messageDao.findById(id);
    }

    @Override
    public Message save(Message message) {
        return messageDao.save(message);
    }

    @Override
    public void deleteById(int id) {
        messageDao.deleteById(id);
    }

    @Override
    public List<Message> findAllByConversationId(int conversationId) {
        return messageDao.findAllByConversationId(conversationId);
    }
}

