
package com.dev.utils;

import com.dev.objects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
        this.createAdmin();
    }




    public User getUserByUsername(String username) {
        User found = null;
        Session session = sessionFactory.openSession();
        found = (User) session.createQuery("FROM User WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();
        return found;
    }

    public int saveUser(User user) {
        Session session = sessionFactory.openSession();
        int userId = (int) session.save(user);
        session.close();
        return userId;
    }

    public void createAmountUser(CreditManagement creditManagement) {
        Session session = sessionFactory.openSession();
        session.save(creditManagement);
        session.close();

    }


    public User getUserByUsernameAndToken(String username, String token) {
        User found = null;
        Session session = sessionFactory.openSession();
        found = (User) session.createQuery("FROM User WHERE username = :username " +
                "AND token = :token")
                .setParameter("username", username)
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        return found;
    }

    public List<User> getAllUsers() {
        boolean admin = false;
        Session session = sessionFactory.openSession();
        List<User> allUsersNoAdmin = session.createQuery("FROM User WHERE isAdmin = :admin")
                .setParameter("admin", admin)
                .list();
        session.close();
        return allUsersNoAdmin;
    }

    public List<Product> getAllOpenOrCloseTrades(boolean openToAction) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE openToAction = :openToAction")
                .setParameter("openToAction", openToAction)
                .list();
        session.close();
        return products;
    }

    public List<Action> getAllOpenOrCloseActions(Boolean openToAction) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.openToAction   = :openToAction")
                .setParameter("openToAction", openToAction)
                .list();
        session.close();
        return actions;
    }

    public CreditManagement getCreditManagement(int userId) {
        Session session = sessionFactory.openSession();
        CreditManagement creditManagement = (CreditManagement) session.createQuery("FROM CreditManagement WHERE user.id = :userId")
                .setParameter("userId", userId)
                .uniqueResult();
        session.close();
        return creditManagement;
    }

       public List<Product> getMyProductsOpenToActionByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE user.id = :userId AND openToAction = true")
                .setParameter("userId", userId)
                .list();
        session.close();
        return products;
    }



    public int saveProduct(Product product) {
        Session session = sessionFactory.openSession();
        int productId = (int) session.save(product);
        session.close();
        return productId;
    }

    public Product getProductById(int id) {
        Session session = sessionFactory.openSession();
        Product product = (Product) session.createQuery("FROM Product WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return product;
    }

    public Product getProductByProductIdAndUserId(int id, int userId) {
        Session session = sessionFactory.openSession();
        Product product = (Product) session.createQuery("FROM Product WHERE id = :id AND user.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .uniqueResult();
        session.close();
        return product;
    }

    public  Action getActionByProductIdAndUserId(int id, int userId, boolean lastOffer) {
        Session session = sessionFactory.openSession();
        Action action = (Action) session.createQuery("FROM Action WHERE product.id = :id AND userSuggest.id = :userId AND lastOffer = :lastOffer")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("lastOffer", lastOffer)
                .uniqueResult();
        session.close();
        return action;
    }


    public List<Action> getActionsByProductId(int id) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.id = :id")
                .setParameter("id", id)
                .list();
        session.close();
        return actions;
    }

    public List<Action> getActionsByProductIdAndUserId(int id, int userId) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.id = :id AND userSuggest.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .list();
        session.close();
        return actions;
    }

    public List<Action> updateWinnerActionsByProductIdMaxAmountLastOffer(int id) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.id = :id " +
                "AND userSuggestAmount = (SELECT MAX(userSuggestAmount) FROM Action WHERE product.id = :id " +
                "AND lastOffer = :lastOffer) AND lastOffer = :lastOffer")
                .setParameter("id", id)
                .setParameter("lastOffer", true)
                .list();
        session.close();
        return actions;

    }


    public void updateCreditManagementToLoserActionByProductId(int id, boolean lastOffer, User userSuggest) {
        Session session = sessionFactory.openSession();
        List<Action> action = (List<Action>) session.createQuery("FROM Action WHERE product.id = :id " +
                "AND userSuggest.id != :userId"
                + " AND lastOffer = :lastOffer")
                .setParameter("id", id)
                .setParameter("userId", userSuggest.getId()).
                        setParameter("lastOffer", lastOffer)
                .list();
        session.close();

        for (Action action1 : action) {
            action1.setWinner(Constants.LOSS);
            updateAction(action1);
            CreditManagement creditManagement = getCreditManagement(action1.getUserSuggest().getId());
            creditManagement.setCreditAmount(creditManagement.getCreditAmount() + action1.getUserSuggestAmount());
            updateCreditManagement(creditManagement);

        }

    }


    public void updateCreditManagement(CreditManagement creditManagement) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(creditManagement);
        session.getTransaction().commit();
        session.close();
        ;
    }


    public void updateProduct(Product product) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(product);
        session.getTransaction().commit();
        session.close();
    }

    public int saveAction(Action action) {
        Session session = sessionFactory.openSession();
        int id = (int) session.save(action);
        session.close();
        return id;
    }

    public List<Action> getActionsByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE userSuggest.id = :userId")
                .setParameter("userId", userId)
                .list();
        session.close();
        return actions;
    }


    public Action getActionByProductIdAndUserIdAndLastOffer(int id, int userId, boolean lastOffer) {
        Session session = sessionFactory.openSession();
        Action action = (Action) session.createQuery("FROM Action WHERE product.id = :id AND userSuggest.id = :userId AND lastOffer = :lastOffer")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .setParameter("lastOffer", lastOffer)
                .uniqueResult();
        session.close();
        return action;
    }

    public Action getWinnerActionsByProductIdMaxAmountAndMinDate(int id) {
        Session session = sessionFactory.openSession();
        Action action = (Action) session.createQuery("FROM Action WHERE product.id = :id " +
                "AND userSuggestAmount = (SELECT MAX(userSuggestAmount) FROM Action WHERE product.id = :id " +
                "AND lastOffer = :lastOffer) AND lastOffer = :lastOffer " +
                "AND biddingDate = (SELECT MIN(biddingDate) FROM Action WHERE product.id = :id " +
                "AND lastOffer = :lastOffer AND userSuggestAmount = (SELECT MAX(userSuggestAmount) FROM Action WHERE product.id = :id " +
                "AND lastOffer = :lastOffer))")
                .setParameter("id", id)
                .setParameter("lastOffer", true)
                .uniqueResult();
        session.close();
        return action;

    }



    public void updateAction(Action action) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(action);
        session.getTransaction().commit();
        session.close();
    }

    public List<Product> getProductsByUserId(int publisherId) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE user.id = :publisherId")
                .setParameter("publisherId", publisherId)
                .list();
        session.close();
        return products;

    }


    public User getUserByToken(String token) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("From User WHERE token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        return user;
    }


    public  List<Product> getProductsOpenToActionByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE user.id != :userId AND openToAction = :openToAction")
                .setParameter("userId", userId)
                .setParameter("openToAction", true)
                .list();
        session.close();
        return products;
    }

    public List<Product> getProductsOpenToAction() {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE openToAction = :openToAction")
                .setParameter("openToAction", true)
                .list();
        session.close();
        return products;
    }



    public List<Action> getGeneralBidsByProductId(int productId) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE isWinner = :isWinner AND product.id = :productId")
                .setParameter("isWinner", Constants.NO_RESULT)
                .setParameter("productId", productId)
                .list();
        session.close();
        return actions;
    }






    public List<Action> getOpenActionsByUserId(int userId ) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE publisher.id != :userId AND isWinner = :isWinner")
                .setParameter("userId", userId)
                .setParameter("isWinner", Constants.NO_RESULT)
                .list();
        session.close();
        return actions;
    }


    public User getUserById(int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("FROM User WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return user;
    }

    public void  createAdmin(){
        User admin = this.getUserByUsername("admin");
        if (admin == null){
            Utils utils = new Utils();
            String token = utils.createHash("admin","123456");
            admin = new User("admin",token,true);
            int id=  this.saveUser(admin);
            admin.setId(id);
            CreditManagement creditManagement = new CreditManagement(0,admin);
            this.createAmountUser(creditManagement);
        }

    }


}
