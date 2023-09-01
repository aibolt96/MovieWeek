package com.javaunit3.springmvc;

import org.hibernate.SessionFactory;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "voters_name")
    private String votersName;

    SessionFactory factory = new org.hibernate.cfg.Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(MovieEntity.class)
            .addAnnotatedClass(VoteEntity.class)
            .buildSessionFactory();

    public String getVoterName() {
        return null;
    }

    public void setVotersName(String votersName) {
    }
}
