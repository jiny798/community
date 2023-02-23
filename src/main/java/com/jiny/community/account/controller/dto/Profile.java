package com.jiny.community.account.controller.dto;

import com.jiny.community.account.domain.Account;
import com.jiny.community.account.domain.CommonAttribute;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Length(max = 35)
    private String bio;
    @Length(max = 50)
    private String company;
    private String image;

    public static Profile from(Account account){
        return new Profile(account);
    }
    protected Profile(Account account){
        Optional<CommonAttribute.Profile> optionalAccount = Optional.ofNullable(account.getProfile());
        this.bio= Optional.ofNullable(account.getProfile()).map(CommonAttribute.Profile :: getBio).orElse(null);
        this.company= Optional.ofNullable(account.getProfile()).map(CommonAttribute.Profile :: getCompany).orElse(null);
        this.image = Optional.ofNullable(account.getProfile()).map(CommonAttribute.Profile::getImage).orElse(null);
    }

}
