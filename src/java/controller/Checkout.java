/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.OrderItem;
import entity.OrderStatus;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sande
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();
        HttpSession httpSession = request.getSession();
        Transaction transaction = session.beginTransaction();
        
        boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String city_id = requestJsonObject.get("city_id").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String postal_code = requestJsonObject.get("postal_code").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();

        if (httpSession.getAttribute("user") != null) {
            //user signed in
            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (isCurrentAddress) {
                //getCurrent address
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //current address not found. please create a new address
                    responseJsonObject.addProperty("message", "Current address not found. Please create a new address");

                } else {
                    //Get the current address
                    Address address = (Address) criteria2.list().get(0);
                    
                    saveOrder(session, transaction, user, address, responseJsonObject);
                }
            } else {
                //create new address

                //get cart items
                //Complete the checkout process
                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill first name");
                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill last name");
                } else if (!Validations.isInteger(city_id)) {
                    responseJsonObject.addProperty("message", "Invalid City");
                } else {
                    //check city from db
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(city_id)));

                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid City selected");

                    } else {
                        //city found
                        City city = (City) criteria3.list().get(0);

                        if (address1.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill line 1");

                        } else if (address2.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill line 2");

                        } else if (postal_code.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill postal code");

                        } else if (postal_code.length() != 5) {
                            responseJsonObject.addProperty("message", "Invalid postal code");

                        } else if (!Validations.isInteger(postal_code)) {
                            responseJsonObject.addProperty("message", "Invalid postal code");

                        } else if (mobile.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill mobile");

                        } else if (!Validations.isMobileNumberValid(mobile)) {
                            responseJsonObject.addProperty("message", "Invalid mobile");

                        } else {
                            //create new address
                            Address address = new Address();
                            address.setCity(city);
                            address.setFirst_name(first_name);
                            address.setLast_name(last_name);
                            address.setLine1(address1);
                            address.setLine2(address2);
                            address.setMobile(mobile);
                            address.setPostal_code(postal_code);
                            address.setUser(user);

                            session.save(address);
                            
                            //**Complete the chechout process**\\//
                            saveOrder(session, transaction, user, address, responseJsonObject);
                            responseJsonObject.addProperty("message", "Complete the chechout process");
                            responseJsonObject.addProperty("success", true);
                            
                        }

                    }

                }

            }

        } else {
            //user not
            responseJsonObject.addProperty("message", "User not signed in");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
        session.close();
    }
    
    private void saveOrder(Session session,Transaction transaction,User user,Address address,JsonObject responseJsonObject) {
        try {
            //Create Oder in db
            entity.Orders order = new entity.Orders();
            order.setAddress(address);
            order.setDate_time(new Date());
            order.setUser(user);

            int order_id = (int) session.save(order);

            //get cart items
            Criteria criteria3 = session.createCriteria(Cart.class);
            criteria3.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria3.list();
           
            //get Orders Status(5 - pending payment)
            OrderStatus order_Status = (OrderStatus) session.get(OrderStatus.class, 5);

            //create order item
            double amount = 0;
            String items ="";
            for (Cart cartItem : cartList) {
                
                //calculate amount
                amount += cartItem.getQty()*cartItem.getProduct().getPrice();
                if(address.getCity().getId()==5){
                    amount+=1000;
                }else{
                    amount+=2500;
                }
                
                //get item details
                items += cartItem.getProduct().getTitle()+" x"+cartItem.getQty();
                
                Product product = cartItem.getProduct();

                OrderItem order_Item = new OrderItem();
                order_Item.setOrder(order);
                order_Item.setOrder_status(order_Status);
                order_Item.setProduct(product);
                order_Item.setQty(cartItem.getQty());

                //save order items
                session.save(order_Item);
                //Update Prduct qty
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product);

                //Delete Cart Item from DB
                session.delete(cartItem);
            }
            transaction.commit();
            
            //Start: set payment data
            String merchnt_id="1223784";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "MjgzNjExMjg3ODE2NjQxMzc1MzMzODEwMTI3ODU0MzM0MjU0NDMyOA==";
            String merchantSecretMd5Hash = PayHere.generateMD5(merchantSecret);
            
            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchnt_id);
            
            payhere.addProperty("sandbox", true);
            
            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "");
            
            payhere.addProperty("first_name", user.getFirst_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", "");
            payhere.addProperty("address", "");
            payhere.addProperty("city", "");
            payhere.addProperty("country", "");
            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formatedAmount);
            
            //generate MD5 Hash
            String md5Hash = PayHere.generateMD5(merchnt_id + order_id + formatedAmount + currency + merchantSecretMd5Hash);
            payhere.addProperty("hash", md5Hash);
            //End: ser payment data
            
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Checkout Complet");
            
            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));
            System.out.println(gson.toJsonTree(payhere));
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
