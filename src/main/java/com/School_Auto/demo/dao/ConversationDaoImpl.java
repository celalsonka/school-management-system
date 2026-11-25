package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Conversation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ConversationDaoImpl implements ConversationDao {

    private EntityManager entityManager;

    @Autowired
    public ConversationDaoImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public List<Conversation> findAll() {
        TypedQuery<Conversation> theQuery = entityManager.createQuery("FROM Conversation", Conversation.class);
        List<Conversation> conversations = theQuery.getResultList();
        return conversations;
    }

    @Override
    public Conversation findById(int theId) {
        Conversation theConversation = entityManager.find(Conversation.class, theId);
        return theConversation;
    }

    @Override
    public Conversation findById1AndAd2(int theId1, int theId2) {
        TypedQuery<Conversation> query = entityManager.createQuery(
                "FROM Conversation c WHERE (c.person1Id = :p1 AND c.person2Id = :p2) " +
                        "OR (c.person1Id = :p2 AND c.person2Id = :p1)", Conversation.class);
        query.setParameter("p1", theId1);
        query.setParameter("p2", theId2);

        List<Conversation> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    @Override
    public Conversation save(Conversation theConversation) {
        Conversation dbConversation = entityManager.merge(theConversation);
        return dbConversation;
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        Conversation theConversation = entityManager.find(Conversation.class, theId);
        entityManager.remove(theConversation);
    }
}
