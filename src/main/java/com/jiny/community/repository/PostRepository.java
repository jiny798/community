package com.jiny.community.repository;

import com.jiny.community.domain.Post;
import com.jiny.community.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepository {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long save(Post post){
        em.persist(post);
        return post.getId();
    }
    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    /**
    * 게시글 제목으로 검색하여 게시글 엔티티 가져오기
     */
    public List<Post> findByName(String title) {
        return em.createQuery("select p from Post p where p.title = :title",
                        Post.class)
                .setParameter("title", title)
                .getResultList();
    }


}
