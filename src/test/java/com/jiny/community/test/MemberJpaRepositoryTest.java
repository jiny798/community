package com.jiny.community.test;




import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Autowired
    TeamRepository teamRepository;

//    @Test
//    public void DataJpaTest(){
//        Member memberA = new Member("memberA");
//        Member memberB = new Member("memberB");
//
//        //저장
//        Member saveMemberA = memberRepository.save(memberA);
//        Member saveMemberB = memberRepository.save(memberB);
//
//        //검색
//        Member findMemberA = memberRepository.findById(saveMemberA.getId()).get();
//
//        Assertions.assertThat(saveMemberA.getId()).isEqualTo(memberA.getId());
//        Assertions.assertThat(saveMemberA.getUsername()).isEqualTo(memberA.getUsername());
//        Assertions.assertThat(saveMemberA).isEqualTo(memberA);
//
//        //전체 조회
//        List<Member> members = memberRepository.findAll();
//        Assertions.assertThat(members.size()).isEqualTo(2);
//
//        //삭제
//        memberRepository.delete(memberA);
//
//        long count = memberRepository.count();
//        Assertions.assertThat(count).isEqualTo(1);
//
//        //쿼리메서드
//        boolean tag = memberRepository.existsByUsername("memberB");
//        Assertions.assertThat(tag).isEqualTo(true);
//    }

    @Test
    @Transactional
    public void DataJpaTest2(){
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);
        memberRepository.save(new Member("memberA", 10, teamA));
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByUsername("memberA");
        findMember.setAge(15);
        em.flush();
        em.clear();

        Member findMember2 = memberRepository.findByUsername("memberA");
        System.out.println("findMember2의 나이는 그대로 10을 유지해야한다 -> "+ findMember2.getAge());

//        List<Member> members = memberRepository.findAll();
//        for (Member member : members){
//            member.getTeam().getName();
//        }
    }


}