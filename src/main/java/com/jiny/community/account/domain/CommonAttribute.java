package com.jiny.community.account.domain;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.util.Optional;

public abstract class CommonAttribute {

    @Embeddable
    @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class Profile {
        protected String bio;
        protected String company;
        @Lob @Basic(fetch = FetchType.EAGER)
        protected String image;
        public void setAttribute(String bio,String company){
            this.bio = bio;
            this.company = company;
        }

    }



}
