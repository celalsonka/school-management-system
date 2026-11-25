package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Message;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Message> findAll() {
        return entityManager.createQuery("FROM Message", Message.class).getResultList();
    }

    @Override
    public Message findById(int id) {
        return entityManager.find(Message.class, id);
    }

    @Transactional
    @Override
    public Message save(Message message) {
        return entityManager.merge(message);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        Message message = findById(id);
        if (message != null) {
            entityManager.remove(message);
        }
    }

    @Override
    public List<Message> findAllByConversationId(int conversationId) {
        return entityManager.createQuery(
                        "FROM Message WHERE conversation.id = :conversationId ORDER BY timestamp ASC",
                        Message.class
                ).setParameter("conversationId", conversationId)
                .getResultList();
    }
}
