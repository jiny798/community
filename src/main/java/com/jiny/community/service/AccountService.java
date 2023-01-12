package com.jiny.community.service;

import com.jiny.community.domain.Account;
import com.jiny.community.domain.UserAccount;
import com.jiny.community.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired AccountRepository accountRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByNickname(username);
        if(account ==null){
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
        // User를 구현한 UserAccount를 리턴
        // User는 Userdetails를 상속받음
    }
}
