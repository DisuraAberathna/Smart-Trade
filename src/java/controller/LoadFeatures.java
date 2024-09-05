/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.ProductCondition;
import entity.Storage;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author SINGER
 */
@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria1.list();

        Criteria criteria2 = session.createCriteria(Model.class);
        criteria2.addOrder(Order.asc(""));
        List<Model> modelList = criteria2.list();

        Criteria criteria3 = session.createCriteria(Color.class);
        criteria3.addOrder(Order.asc(""));
        List<Color> colorList = criteria3.list();

        Criteria criteria4 = session.createCriteria(Storage.class);
        criteria4.addOrder(Order.asc(""));
        List<Storage> storageList = criteria4.list();

        Criteria criteria5 = session.createCriteria(ProductCondition.class);
        criteria5.addOrder(Order.asc(""));
        List<ProductCondition> conditionList = criteria5.list();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("modelList", gson.toJsonTree(modelList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        jsonObject.add("storageList", gson.toJsonTree(storageList));
        jsonObject.add("conditionList", gson.toJsonTree(conditionList));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));

        session.close();
    }

}
