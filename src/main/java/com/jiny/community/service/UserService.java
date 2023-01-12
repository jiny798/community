package com.jiny.community.service;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.UserLikePost;
import com.jiny.community.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;
    /*
    회원가입
     */
    @Transactional
    public Long join(Account account) {
        validateDuplicateMember(account); //중복 회원 검증
        accountRepository.save(account);
        return account.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Account account) {
        List<Account> findMember = accountRepository.findByName(account.getEmail());
        if (!findMember.isEmpty() ) {
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
        return accountRepository.findOne(memberId);
    }

    @Transactional
    public void updateLikePost(Account user , UserLikePost userLikePost) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        //User user = userRepository.find(userId);
        user.getUserLikePosts().add(userLikePost); // 트랜잭션이 끝나면 자동으로 저장 된다
    }

}
