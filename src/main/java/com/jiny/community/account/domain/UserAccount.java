package com.jiny.community.account.domain;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserAccount extends User {

    @Getter //AuthenticationPrincipal Principal에서(account) 가져올때 getter 필요
    private Account account;

    public UserAccount(Account account){
        super(account.getNickname(), account.getPassword(),
                List.of(new SimpleGrantedAuthority( "ROLE_"+account.getRole()) )  );
        this.account = account;
    }

    public String getAccountNickName(){
        return account.getNickname();
    }

    public Long getAccountId(){return account.getId();}

    public String getAccountEmail(){return account.getEmail();}
}
