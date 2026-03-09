package com.example;

import com.example.entity.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        // 1. Insert
        Product p1 = new Product("Laptop", "Gaming Laptop", 1200.0, 10);
        Product p2 = new Product("Phone", "Android Phone", 500.0, 20);
        Product p3 = new Product("Book", "Fictional", 120.0, 2);
        Product p4 = new Product("Pens", "Blue Ball", 5.0, 5);
        Product p5 = new Product("Pencil", "0.5 lead", 10.0, 3);
        Product p6 = new Product("Phone", "Apple Phone", 50000.0, 1);
        Product p7 = new Product("Laptop", "Education Laptop", 72000.0, 10);
        session.save(p1);
        session.save(p2);
        session.save(p3);
        session.save(p4);
        session.save(p5);
        session.save(p6);
        session.save(p7);

        System.out.println("\n--- Sorting by Price ASC ---");
        List<Product> asc = session.createQuery("FROM Product p ORDER BY p.price ASC", Product.class).list();
        asc.forEach(System.out::println);

        System.out.println("\n--- Sorting by Price DESC ---");
        List<Product> desc = session.createQuery("FROM Product p ORDER BY p.price DESC", Product.class).list();
        desc.forEach(System.out::println);

        System.out.println("\n--- Sorting by Quantity DESC ---");
        List<Product> qty = session.createQuery("FROM Product p ORDER BY p.quantity DESC", Product.class).list();
        qty.forEach(System.out::println);

        // --- Pagination ---
        System.out.println("\n--- First 3 Products ---");
        List<Product> first3 = session.createQuery("FROM Product", Product.class)
                .setFirstResult(0).setMaxResults(3).list();
        first3.forEach(System.out::println);

        System.out.println("\n--- Next 3 Products ---");
        List<Product> next3 = session.createQuery("FROM Product", Product.class)
                .setFirstResult(3).setMaxResults(3).list();
        next3.forEach(System.out::println);

        // --- Aggregates ---
        Long total = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).uniqueResult();
        System.out.println("\nTotal products: " + total);

        Long available = session.createQuery("SELECT COUNT(p) FROM Product p WHERE p.quantity > 0", Long.class).uniqueResult();
        System.out.println("Products with quantity > 0: " + available);

        List<Object[]> grouped = session.createQuery("SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description").list();
        System.out.println("\nCount grouped by description:");
        for (Object[] row : grouped) {
            System.out.println(row[0] + " -> " + row[1]);
        }

        Double minPrice = session.createQuery("SELECT MIN(p.price) FROM Product p", Double.class).uniqueResult();
        Double maxPrice = session.createQuery("SELECT MAX(p.price) FROM Product p", Double.class).uniqueResult();
        System.out.println("\nMin price: " + minPrice + ", Max price: " + maxPrice);

        // --- Group By ---
        System.out.println("\n--- Group By Description ---");
        List<Object[]> groupDesc = session.createQuery("SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description").list();
        for (Object[] row : groupDesc) {
            System.out.println(row[0] + " -> " + row[1]);
        }

        // --- Filtering (WHERE) ---
        System.out.println("\n--- Products in Price Range 500-1000 ---");
        List<Product> range = session.createQuery("FROM Product p WHERE p.price BETWEEN 500 AND 1000", Product.class).list();
        range.forEach(System.out::println);

        // --- LIKE Queries ---
        System.out.println("\n--- Names starting with 'L' ---");
        List<Product> startL = session.createQuery("FROM Product p WHERE p.name LIKE 'L%'", Product.class).list();
        startL.forEach(System.out::println);

        System.out.println("\n--- Names ending with 'e' ---");
        List<Product> endE = session.createQuery("FROM Product p WHERE p.name LIKE '%e'", Product.class).list();
        endE.forEach(System.out::println);

        System.out.println("\n--- Names containing 'top' ---");
        List<Product> containsTop = session.createQuery("FROM Product p WHERE p.name LIKE '%top%'", Product.class).list();
        containsTop.forEach(System.out::println);

        System.out.println("\n--- Names with exact length 5 ---");
        List<Product> length5 = session.createQuery("FROM Product p WHERE LENGTH(p.name) = 5", Product.class).list();
        length5.forEach(System.out::println);

        // 2. Retrieve
//        Product retrieved = session.get(Product.class, p1.getId());
//        System.out.println("Retrieved: " + retrieved);
//
//        // 3. Update
//        retrieved.setPrice(1100.0);
//        retrieved.setQuantity(8);
//        session.update(retrieved);
//
//        // 4. Delete
//        session.delete(p2);

        tx.commit();
        session.close();
        HibernateUtil.shutdown();
    }
}