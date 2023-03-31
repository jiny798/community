package com.jiny.community.account.service;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.repository.UserLikePostRepository;
import com.jiny.community.board.domain.Post;
import com.jiny.community.account.domain.UserLikePost;
import com.jiny.community.account.repository.AccountRepository;
import com.jiny.community.board.repository.PostRepository;
import com.jiny.community.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final UserLikePostRepository userLikePostRepository;


    @Autowired
    PasswordEncoder passwordEncoder;
    /*
    회원가입
     */
    @Transactional
    public Long join(Account account) {
        validateDuplicateMember(account); //중복 회원 검증
        account.encodePassword(passwordEncoder);
        accountRepository.save(account);
        return account.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Account account) {
        Account findMember = accountRepository.findByEmail(account.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Account> findMembers() {
        return accountRepository.findAll();
    }
    /**
    id로 조회
     */
    public Account findOne(Long memberId) {
        return accountRepository.findById(memberId).get();
    }

    @Transactional
    public void updateLikePost(Long accountId , Long postId) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Account account=accountRepository.findById(accountId).get();
        Post post=postRepository.findById(postId).get();
        boolean isLike = false;

        List<UserLikePost> list=account.getUserLikePosts();
        int index =0;
        for (int i=0;i<list.size();i++){
            if (post.getId() == list.get(i).getPost().getId() ){
                isLike = true;
                index = i;
                break;
            }
        }

        if(!isLike) { // 좋아요를 하지 않은 게시물이라면
            // UserLikePost - Post 연결
            UserLikePost userLikePost = UserLikePost.createLikePost(post);
            // UserLikePost - User 연결
            account.addLikePost(userLikePost);
            post.addStar(); // star 증가
        }else{
            post.decreaseStar(); // star 감소
            userLikePostRepository.deleteById(list.get(index).getId()); // 해당 게시물 좋아요 리스트에서 제거
            list.remove(index); //account 연관 관계 list에서도 제거하여 더티체킹 방지
        }
    }
    @Transactional
    public boolean isLikePost(Long accountId , Long postId) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Account account=accountRepository.findById(accountId).get();
        Post post=postRepository.findById(postId).get();
        boolean isLike = false;
        List<UserLikePost> list=account.getUserLikePosts();
        for (int i=0;i<list.size();i++){
            if (post.getId() == list.get(i).getPost().getId() ){
                isLike = true;
                break;
            }
        }
       return isLike;
    }


}
