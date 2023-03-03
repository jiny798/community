package com.jiny.community.test;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {



    long countById(Long id);

    boolean existsById(Long id);
    boolean existsByUsername(String username);

    void deleteById(Long id);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findByUsername(String username);

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
}
