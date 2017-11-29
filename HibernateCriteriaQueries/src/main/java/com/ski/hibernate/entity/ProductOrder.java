package com.ski.hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_ORDER")
public class ProductOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    int no;

    @ManyToOne(cascade = CascadeType.ALL)
    Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    Product product;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
