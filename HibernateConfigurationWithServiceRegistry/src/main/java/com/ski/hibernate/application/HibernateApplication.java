package com.ski.hibernate.application;

import com.ski.hibernate.config.HibernateConfiguration;
import com.ski.hibernate.entity.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

public class HibernateApplication {

    public static void main(String args[]) {
       SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
        listStudentsWithUsingEntityBean(sessionFactory);
        //listStudentsWithoutUsingEntityBean(sessionFactory);
    }


    public static void listStudentsWithoutUsingEntityBean (SessionFactory sessionFactory) {
        Session session  = sessionFactory.openSession();
        session.beginTransaction();

        String sql = "select * from STUDENT";
        //String result = ((Object[])session.createNativeQuery(sql).getResultList().get(0))[0].toString();

        Object result[] = ((Object[])session.createNativeQuery(sql).getResultList().get(0));
        System.out.print(result[0].toString());
        System.out.print("\t");
        System.out.print(result[1].toString());
        System.out.print("\t");
        System.out.print(result[2].toString());
        System.out.print("\t");
        System.out.print(result[3].toString());
        System.out.print("\t");
        System.out.print(result[4].toString());

        session.getTransaction().commit();;
        session.close();
        HibernateConfiguration.shutDown();

    }

    public static void listStudentsWithUsingEntityBean(SessionFactory sessionFactory){
        Session session  = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List students = session.createQuery("FROM Student").list();
            for (Iterator iterator = students.iterator(); iterator.hasNext();){
                Student student = (Student) iterator.next();
                System.out.println("First Name: " + student.getFirstName() + " Last Name: " + student.getLastName() + "  Email: " + student.getEmail());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
