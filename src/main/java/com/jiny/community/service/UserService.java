package com.jiny.community.service;

import com.jiny.community.domain.User;
import com.jiny.community.domain.UserLikePost;
import com.jiny.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    /*
    회원가입
     */
    @Transactional
    public Long join(User user) {
        validateDuplicateMember(user); //중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(User user) {
        List<User> findMember = userRepository.findByName(user.getEmail());
        if (!findMember.isEmpty() ) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    /**
    id로 조회
     */
    public User findOne(Long memberId) {
        return userRepository.findOne(memberId);
    }

    @Transactional
    public void updateLikePost(User user , UserLikePost userLikePost) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        //User user = userRepository.find(userId);
        user.getUserLikePosts().add(userLikePost); // 트랜잭션이 끝나면 자동으로 저장 된다
    }

}
