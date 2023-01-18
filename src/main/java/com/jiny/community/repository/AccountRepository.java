package com.jiny.community.repository;

import com.jiny.community.domain.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccountRepository {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public Account save(Account account){
        em.persist(account);
        return account;
    }

    public Account find(Long id){
        return em.find(Account.class,id);
    }
    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }
    public List<Account> findAll() {
        return em.createQuery("select ac from Account ac", Account.class)
                .getResultList();
    }

    public Account findByEmail(String email) {

        List<Account> accounts =em.createQuery("select ac from Account ac where ac.email = :email", Account.class)
                .setParameter("email", email)
                .getResultList();
        if(accounts.size()==0){
            return null;
        }else{
            return accounts.get(0);
        }

    }

    public Account findByNickname(String username){

        List<Account> accounts2 =em.createQuery("select ac from Account ac where ac.nickname = :nickname", Account.class)
                .setParameter("nickname", username)
                .getResultList();
        if(accounts2.size()==0){
            return null;
        }else{
            return accounts2.get(0);
        }
    }
}
