package com.example.redispub.entity;


import jakarta.persistence.*;



@Entity
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ROOM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATOR_ID", referencedColumnName = "MEMBER_ID")
    private Member creator;


    public Room() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }
}
