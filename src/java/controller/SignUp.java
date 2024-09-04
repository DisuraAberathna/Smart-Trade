/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ResponseDTO;
import dto.UserDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author SINGER
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        UserDTO userDTO = gson.fromJson(req.getReader(), UserDTO.class);

        if (userDTO.getFirst_name().isEmpty()) {
            responseDTO.setContent("Please enter your first name");
        } else if (userDTO.getLast_name().isEmpty()) {
            responseDTO.setContent("Please enter your last name");
        } else if (userDTO.getEmail().isEmpty()) {
            responseDTO.setContent("Please enter your email");
        } else if (!Validations.isEmailValid(userDTO.getPassword())) {
            responseDTO.setContent("Please enter valid email");
        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setContent("Please enter your password");
        } else if (!Validations.isPasswordValid(userDTO.getPassword())) {
            responseDTO.setContent("Please enter valid password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", userDTO.getEmail()));

            if (!criteria.list().isEmpty()) {
                responseDTO.setContent("User with this email already exists");
            } else {
                int code = (int) (Math.random() * 1000000);

                User user = new User();
                user.setEmail(userDTO.getEmail());
                user.setFirst_name(userDTO.getFirst_name());
                user.setLast_name(userDTO.getLast_name());
                user.setPassword(userDTO.getPassword());
                user.setVerification(String.valueOf(code));

                Thread sendMailThread = new Thread() {
                    @Override
                    public void run() {
                        Mail.sendMail(userDTO.getEmail(), "Smart Trade Verification", "<h1> Your Verification Code: " + user.getVerification() + "</h1>");
                    }
                };
                sendMailThread.start();

                session.save(user);
                session.beginTransaction().commit();

                req.getSession().setAttribute("email", userDTO.getEmail());
                responseDTO.setSuccess(true);
                responseDTO.setContent("Successfully registered. Please check your inbox for verification code!");
            }

            session.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
    }

}
