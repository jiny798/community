package com.jiny.community.account.domain;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.util.Optional;

public abstract class CommonAttribute {

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class Profile {
        private String bio;
        private String company;
        @Lob @Basic(fetch = FetchType.EAGER)
        private String image;

        public static Profile from(Account account){
            return new Profile(account);
        }
        protected Profile(Account account){
            Optional<Profile> optionalAccount = Optional.ofNullable(account.getProfile());
            this.bio= Optional.ofNullable(account.getProfile()).map(CommonAttribute.Profile :: getBio).orElse(null);
            this.company= Optional.ofNullable(account.getProfile()).map(CommonAttribute.Profile :: getCompany).orElse(null);
        }
    }



}
