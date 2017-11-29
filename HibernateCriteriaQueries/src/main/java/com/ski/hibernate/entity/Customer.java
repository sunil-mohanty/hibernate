package com.ski.hibernate.entity;

import javax.persistence.*;


@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "id")
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
