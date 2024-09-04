/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        
        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(req.getReader(), JsonObject.class);
        String verification = dto.get("verification").getAsString();
        
        if (req.getSession().getAttribute("email") != null) {
            String email = req.getSession().getAttribute("email").toString();
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("verification", verification));
            
            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.list().get(0);
                user.setVerification("Verified");
                
                session.update(user);
                session.beginTransaction().commit();
                
                UserDTO userDTO = new UserDTO();
                userDTO.setFirst_name(user.getFirst_name());
                userDTO.setLast_name(user.getLast_name());
                userDTO.setEmail(email);
                req.getSession().removeAttribute("email");
                req.getSession().setAttribute("user", userDTO);
                
                responseDTO.setSuccess(true);
                responseDTO.setContent("Verification success");
            } else {
                responseDTO.setContent("Invalid verification code");
            }
        } else {
            responseDTO.setContent("Verification unavailable please sign in");
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
    }
    
}
