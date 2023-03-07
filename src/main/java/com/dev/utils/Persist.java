
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
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public User getUserByUsername (String username) {
        User found = null;
        Session session = sessionFactory.openSession();
        found = (User) session.createQuery("FROM User WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();
        return found;
    }

    public int saveUser (User user) {
        Session session = sessionFactory.openSession();
       int userId = (int)  session.save(user);
        session.close();

        return userId;
    }

    public void createAmountUser (CreditManagement creditManagement){
        Session session = sessionFactory.openSession();
        session.save(creditManagement);
        session.close();

    }




    public User getUserByUsernameAndToken (String username, String token) {
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

    public List<User> getAllUsers () {
        Session session = sessionFactory.openSession();
        List<User> allUsers = session.createQuery("FROM User ").list();
        session.close();
        return allUsers;
    }

    public List<Product> getAllOpenOrCloseTrades (boolean openToAction) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("FROM Product WHERE openToAction = :openToAction")
                .setParameter("openToAction", openToAction)
                .list();
        session.close();
        return products;
    }

    public  List<Action> getAllOpenOrCloseActions (Boolean openToAction){
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.openToAction   = :openToAction")
                .setParameter("openToAction", openToAction)
                .list();
        session.close();
        return actions;
    }

    public  CreditManagement  getCreditManagement (int userId){
        Session session = sessionFactory.openSession();
        CreditManagement creditManagement = (CreditManagement) session.createQuery("FROM CreditManagement WHERE user.id = :userId")
                .setParameter("userId", userId)
                .uniqueResult();
        session.close();
        return creditManagement;
    }

    public void saveProduct (Product product) {
        Session session = sessionFactory.openSession();
        session.save(product);
        session.close();
    }

    public Product getProductById (int id) {
        Session session = sessionFactory.openSession();
        Product product = (Product) session.createQuery("FROM Product WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return product;
    }

    public List<Action>  getActionsByProductId (int id){
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.id = :id")
                .setParameter("id", id)
                .list();
        session.close();
        return actions;
    }

    public List<Action>  getActionsByProductIdAndUserId (int id,int userId){
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE product.id = :id AND userSuggest.id = :userId")
                .setParameter("id", id)
                .setParameter("userId", userId)
                .list();
        session.close();
        return actions;
    }

    public List<Action>  updateWinnerActionsByProductIdMaxAmountLastOffer (int id){
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




    public void updateCreditManagementToLoserActionByProductId(int id,boolean lastOffer,User userSuggest){
        Session session = sessionFactory.openSession();
        List<Action>  action = (List<Action>) session.createQuery("FROM Action WHERE product.id = :id " +
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
            creditManagement.setCreditAmount(creditManagement.getCreditAmount()+action1.getUserSuggestAmount());
            updateCreditManagement(creditManagement);
        }

    }

    public void updateCreditManagement(CreditManagement creditManagement){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(creditManagement);
        session.getTransaction().commit();
        session.close();;
    }




    public void updateProduct (Product product) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(product);
        session.getTransaction().commit();
        session.close();
    }

    public void saveAction (Action action) {
        Session session = sessionFactory.openSession();
        session.save(action);
        session.close();
    }

    public void updateAction (Action action) {
        Session session = sessionFactory.openSession();
        session.update(action);
        session.close();
    }

    public List<Action> getProductActionsByMaxAmount(int publisherId) {
        Session session = sessionFactory.openSession();
        List<Action> actions = session.createQuery("FROM Action WHERE userSuggest.id = :publisherId " +
                "AND userSuggestAmount = (SELECT MAX(userSuggestAmount) FROM Action WHERE userSuggest.id = :publisherId)")
                .setParameter("publisherId", publisherId)
                .list();
        session.close();
        return actions;

    }





    public User getUserByToken (String token) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("From User WHERE token = :token")
                        .setParameter("token", token)
                                .uniqueResult();
        session.close();
        return user;
    }

    public List<Message> getMessagesByToken (String token) {
        Session session = sessionFactory.openSession();
        List<Message> messages = session.createQuery("FROM Message WHERE recipient.token = :token ")
                .setParameter("token", token)
                .list();
        session.close();
        return messages;
    }

    public List<Message> getConversation (String token, int recipientId) {
        Session session = sessionFactory.openSession();
        List<Message> messages = session.createQuery(
                "FROM Message WHERE " +
                        "(sender.token = :token AND recipient.id = :id)" +
                        " OR " +
                        "(sender.id = :id2 AND recipient.token = :token2) ORDER BY id")
                .setParameter("token", token)
                .setParameter("token2", token)
                .setParameter("id", recipientId)
                .setParameter("id2", recipientId)
                .list();
        session.close();
        return messages;
    }

    public User getUserById (int id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.createQuery("FROM User WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return user;
    }

    public void saveMessage (Message message) {
        Session session = sessionFactory.openSession();
        session.save(message);
        session.close();
    }

}
