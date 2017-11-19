package com.ski.hibernate.config;


import com.ski.hibernate.entity.Student;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.util.HashMap;
import java.util.Map;

public class HibernateConfiguration {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;


    public static SessionFactory getSessionFactory() {
        if (sessionFactory ==  null) {
            try  {

                StandardServiceRegistryBuilder registryBuilder =  new StandardServiceRegistryBuilder();
                Map<String, String> settings = new HashMap<>();

                // Settings for Oracle
                /**
                settings.put(Environment.DRIVER, "oracle.jdbc.OracleDriver");
                settings.put(Environment.URL, "jdbc:oracle:thin:@localhost:1521/orcl");
                settings.put(Environment.USER, "c1adb_local");
                settings.put(Environment.PASS, "newpass1");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.Oracle10gDialect");
                **/
                // Settings for Mysql
                settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/c1adb?useSSL=false");
                settings.put(Environment.USER, "c1adb");
                settings.put(Environment.PASS, "newpass1");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");


                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();

                MetadataSources metadataSources = new MetadataSources(registry);
                metadataSources.addAnnotatedClass(Student.class);
                Metadata metadata = metadataSources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch(Exception e){
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutDown() {
        if(registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
