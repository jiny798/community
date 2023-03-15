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
        protected String bio;
        protected String company;
        @Lob @Basic(fetch = FetchType.EAGER) @ToString.Exclude
        protected String image;
        public void setAttribute(String bio,String company){
            this.bio = bio;
            this.company = company;
        }

    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class NotificationSetting {
        protected boolean CommentCreatedByEmail = false;
        protected boolean CommentCreatedByWeb = true;

    }


}
