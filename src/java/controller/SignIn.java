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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author SINGER
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        UserDTO userDTO = gson.fromJson(req.getReader(), UserDTO.class);

        if (userDTO.getEmail().isEmpty()) {
            responseDTO.setContent("Please enter your email");
        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setContent("Please enter your password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", userDTO.getEmail()));
            criteria.add(Restrictions.eq("password", userDTO.getPassword()));

            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.list().get(0);
                
                if (!user.getVerification().equals("Verified")) {
                    req.getSession().setAttribute("email", userDTO.getEmail());
                    responseDTO.setContent("Not Verified");
                } else {
                    userDTO.setFirst_name(user.getFirst_name());
                    userDTO.setLast_name(user.getLast_name());
                    userDTO.setPassword(null);
                    req.getSession().setAttribute("user", userDTO);

                    responseDTO.setSuccess(true);
                    responseDTO.setContent("Successfully sign in");
                }
            } else {
                responseDTO.setContent("Invalid credentials");
            }

            session.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
    }

}
