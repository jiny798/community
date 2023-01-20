package com.jiny.community.repository;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Post;
import com.jiny.community.domain.UserLikePost;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserLikePostRepository {
    @PersistenceContext
    EntityManager em;

    public UserLikePost findOne(Long id) {
        return em.find(UserLikePost.class, id);
    }
    public void delete(Long id){
        em.remove(findOne(id));
    }




}
