package com.jiny.community.board.controller;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.UserLikePost;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.account.repository.UserLikePostRepository;
import com.jiny.community.account.service.UserService;
import com.jiny.community.board.domain.Post;
import com.jiny.community.board.dto.PostForm;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.board.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class PostControllerTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserLikePostRepository userLikePostRepository;

    @Autowired
    PostService postService;
    @PersistenceContext
    EntityManager em;

    @DisplayName("좋아요 동시 테스트")
    @Test
    void likePostMulti() throws InterruptedException {
        Account account1 = accountRepository.findByNickname("abc123");
        Account account2 = accountRepository.findByNickname("abc798");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        PostForm postForm = new PostForm();
        postForm.setTitle("title");
        postForm.setCategory("자유게시판");
        postForm.setContent("Hello");
        Long postId = postService.addPost(account1,postForm);

        executorService.execute(() ->{
            try {
                userService.updateLikePost(account1.getId(), postId);
            }catch (ObjectOptimisticLockingFailureException ex){
                System.out.println("Catch Transaction conflict");
            }
            latch.countDown();
        });
        executorService.execute(() ->{
            try {
                userService.updateLikePost(account2.getId(), postId);
            }catch (ObjectOptimisticLockingFailureException ex){
                System.out.println("Catch Transaction conflict");
            }
            latch.countDown();
        });
        latch.await();

        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);
        //2명이 동시에 좋아요하면 1명은 실패한다
        Assertions.assertThat(post.getStar()).isEqualTo(1);

        List<UserLikePost> uLikePost =userLikePostRepository.findByPost(post);

        //좋아요가 등록된 테이블에도 1명만 있어야 한다.
        Assertions.assertThat(uLikePost.size()).isEqualTo(1);
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

        PostForm postForm = new PostForm();
        postForm.setTitle("title");
        postForm.setCategory("자유게시판");
        postForm.setContent("Hello");
        Long postId = postService.addPost(account,postForm);

        userService.updateLikePost(account.getId(),postId);


        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);

        // 좋아요 개수 1개
        Assertions.assertThat(post.getStar()).isEqualTo(1);

    }
}