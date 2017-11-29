package com.ski.hibernate.application;

import com.ski.hibernate.config.HibernateConfiguration;
import com.ski.hibernate.entity.Customer;
import com.ski.hibernate.entity.Product;
import com.ski.hibernate.entity.ProductOrder;
import org.hibernate.*;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;


public class HibernateApplication {

    public static void main(String args[]) {
       SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
       deleteAllData(sessionFactory);
       insertValues(sessionFactory);
       listProductsWithUsingEntityBean(sessionFactory);
       sessionFactory.close();
    }


    private static void listProductsWithUsingEntityBean(SessionFactory sessionFactory){
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Create CriteriaQuery
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery criteria = builder.createQuery(Integer.class);
            Root<Product> root = criteria.from(Product.class);

            // Let's find out the sum of price of all the products. The below snippet behaves as the earlier approach criteria with projection
            criteria.select(builder.sum(root.get("price")));
            final TypedQuery<Integer> typedQuery = session.createQuery(criteria);
            System.out.println(typedQuery.getSingleResult().longValue());

            //Demonstration of join using Criteria query

            /**
             * Simple SQL "SELECT * from product_order INNER JOIN customer c ON product_order.customer_id = c.id where c.id=901"
             *
             *JPQL "SELECT p from ProductOrder p INNER JOIN Customer c ON p.customer.id = c.id where c.id=901"
             *
             * In the below snippet we will use simple JPQL to fetch the product information
             *
             */

            List<ProductOrder> results = session.createQuery("SELECT p from ProductOrder p INNER JOIN Customer c ON p.customer.id = c.id where c.id=901").getResultList();
            for (ProductOrder order : results) {
                System.out.println("order id = " + order.getProduct().getId() + " "
                        + "price = " + order.getProduct().getPrice());
            }


            /**
             *  let's execute the above stuffs with the help of criteria API
             */
            CriteriaQuery<ProductOrder> query = builder.createQuery(ProductOrder.class);
            Root<ProductOrder> productOrderRoot = query.from(ProductOrder.class);
            Join<ProductOrder, Customer> customer = productOrderRoot.join("customer", JoinType.INNER);
            query.select(productOrderRoot).where(builder.equal(customer.get("id"), 901));

            List<ProductOrder> results1 = session.createQuery(query).getResultList();
            for (ProductOrder order : results1) {
                System.out.println("order id = " + order.getProduct().getId() + " " + "price = " + order.getProduct().getPrice() + " customer id = " + order.getCustomer().getId());
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    private static void deleteAllData(SessionFactory sessionFactory){

        Transaction tx = null;

        try( Session session = sessionFactory.openSession();) {
            tx = session.beginTransaction();

            session.createQuery("delete from Product").executeUpdate();
            session.createQuery("delete from Customer").executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }

    private static void insertValues(SessionFactory sessionFactory) {

       Transaction tx = null;

       try( Session session = sessionFactory.openSession();) {
           tx = session.beginTransaction();

           Customer customer = new Customer();
           customer.setId(901);

           Product product = new Product();
           product.setCategory(101);
           product.setId(11);
           product.setPrice(1001);

           ProductOrder order = new ProductOrder();
           order.setCustomer(customer);
           order.setProduct(product);
           session.persist(order);


           customer = new Customer();
           customer.setId(902);

           product = new Product();
           product.setCategory(102);
           product.setId(12);
           product.setPrice(1002);

           order = new ProductOrder();
           order.setCustomer(customer);
           order.setProduct(product);
           session.persist(order);


           customer = new Customer();
           customer.setId(903);

           product = new Product();
           product.setCategory(103);
           product.setId(13);
           product.setPrice(1003);

           order = new ProductOrder();
           order.setCustomer(customer);
           order.setProduct(product);
           session.persist(order);


           session.getTransaction().commit();
       } catch (HibernateException e) {
           if (tx!=null) tx.rollback();
           e.printStackTrace();
       }
    }

}
