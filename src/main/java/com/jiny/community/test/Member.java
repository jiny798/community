package com.jiny.community.test;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter @Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public  Member(){};
    public Member(String username){
        this(username,0);
    }
    public Member(String username, int age) {
        this(username, age, null);
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }

    }
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
