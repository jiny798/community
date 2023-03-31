package com.jiny.community.account.repository;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.UserLikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface UserLikePostRepository extends JpaRepository<UserLikePost,Long> {
    public List<UserLikePost> findByAccount(Account account);

    public void deleteById(Long id);

}