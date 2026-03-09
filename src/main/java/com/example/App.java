package com.example;

import com.example.entity.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class App {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        // 1. Insert
        Product p1 = new Product("Laptop", "Gaming Laptop", 1200.0, 10);
        Product p2 = new Product("Phone", "Android Phone", 500.0, 20);
        session.save(p1);
        session.save(p2);

        // 2. Retrieve
        Product retrieved = session.get(Product.class, p1.getId());
        System.out.println("Retrieved: " + retrieved);

        // 3. Update
        retrieved.setPrice(1100.0);
        retrieved.setQuantity(8);
        session.update(retrieved);

        // 4. Delete
        session.delete(p2);

        tx.commit();
        session.close();
        HibernateUtil.shutdown();
    }
}