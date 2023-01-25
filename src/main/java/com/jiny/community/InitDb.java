package com.jiny.community;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.Category;
import com.jiny.community.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final AccountService accountService;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            Account account = Account.createAccount("abc798@abc.com","abc798","abc798");
            account.encodePassword(passwordEncoder);
            account.verified();
            account.setRole("ADMIN");

            Category category = new Category();
            category.setName("자유게시판");
            em.persist(category);
            em.persist(account);

        }
    }
}
