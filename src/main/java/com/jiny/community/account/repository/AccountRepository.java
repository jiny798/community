package com.jiny.community.account.repository;

import com.jiny.community.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByEmail(String email);
    Account findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

}
