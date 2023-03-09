package com.jiny.community.board.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.service.AccountService;
import com.jiny.community.account.service.UserService;
import com.jiny.community.board.domain.Post;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.board.service.PostService;
import com.jiny.community.common.controller.common.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @Transactional
class PostControllerTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;
    @PersistenceContext
    EntityManager em;

    @DisplayName("좋아요 동시 테스트")
    @Test @Rollback(value = false)
    void likePostMulti() throws InterruptedException {
        Account account1 = accountRepository.findByNickname("abc123");
        Account account2 = accountRepository.findByNickname("abc798");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executorService.execute(() ->{
            try {
                userService.updateLikePost(account1.getId(), 8L); // DB에 8번 게시물 존재
            }catch (ObjectOptimisticLockingFailureException ex){
                System.out.println("Catch Transaction conflict");
            }
            latch.countDown();
        });
        executorService.execute(() ->{
            try {
                userService.updateLikePost(account2.getId(), 8L); // DB에 8번 게시물 존재
            }catch (ObjectOptimisticLockingFailureException ex){
                System.out.println("Catch Transaction conflict");
            }
            latch.countDown();
        });
        latch.await();

        Post post = postRepository.findById(8L).orElseThrow(RuntimeException::new);
        //2명이 동시에 좋아요하면 1명은 실패한다
        Assertions.assertThat(post.getStar()).isEqualTo(1);

        //removeLike(account1,account2); //좋아요 다시 삭제

    }

    public void removeLike(Account account1,Account account2){
        userService.updateLikePost(account1.getId(),8L);
        userService.updateLikePost(account2.getId(),8L);
        Post post1 = postRepository.findById(8L).get();
        post1.setStar(0L);
    }
    @DisplayName("좋아요 테스트")
    @Test
    void likePost() throws InterruptedException {
        Account account = accountRepository.findByNickname("abc123");
        System.out.println("start ---------------------------");
        userService.updateLikePost(account.getId(),11L); // DB에 11번 게시물 존재

        em.flush();
        em.clear();

        Post post = postRepository.findById(11L).orElseThrow(RuntimeException::new);

        // 좋아요 개수 1개
        Assertions.assertThat(post.getStar()).isEqualTo(1);

    }
}