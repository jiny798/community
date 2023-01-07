package com.jiny.community.repository;

import com.jiny.community.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public User find(Long id){
        return em.find(User.class,id);
    }
    public User findOne(Long id) {
        return em.find(User.class, id);
    }
    public List<User> findAll() {
        return em.createQuery("select m from Member m", User.class)
                .getResultList();
    }

    public List<User> findByName(String email) {
        return em.createQuery("select u from User u where u.email = :email",
                        User.class)
                .setParameter("email", email)
                .getResultList();
    }
}
