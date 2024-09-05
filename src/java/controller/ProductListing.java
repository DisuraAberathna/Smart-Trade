/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.ResponseDTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.ProductCondition;
import entity.Storage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Session;

/**
 *
 * @author SINGER
 */
@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new Gson();

        String categoryId = req.getParameter("category_id");
        String modelId = req.getParameter("model_id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String storageId = req.getParameter("storage_id");
        String colorId = req.getParameter("color_id");
        String conditionId = req.getParameter("condition_id");
        String price = req.getParameter("price");
        String quantity = req.getParameter("quantity");

        Part image1 = req.getPart("img1");
        Part image2 = req.getPart("img2");
        Part image3 = req.getPart("img3");

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (!Validations.isInteger(categoryId)) {
            responseDTO.setContent("Invalid category");
        } else if (!Validations.isInteger(modelId)) {
            responseDTO.setContent("Invalid model");
        } else if (title.isEmpty()) {
            responseDTO.setContent("Please fill Title");
        } else if (description.isEmpty()) {
            responseDTO.setContent("Please fill Description");
        } else if (!Validations.isInteger(storageId)) {
            responseDTO.setContent("Invalid storage");
        } else if (!Validations.isInteger(colorId)) {
            responseDTO.setContent("Invalid color");
        } else if (!Validations.isInteger(conditionId)) {
            responseDTO.setContent("Invalid condition");
        } else if (price.isEmpty()) {
            responseDTO.setContent("Please fill Price");
        } else if (!Validations.isDouble(price)) {
            responseDTO.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            responseDTO.setContent("Price must be greater than 0");
        } else if (quantity.isEmpty()) {
            responseDTO.setContent("Please fill Quantity");
        } else if (!Validations.isInteger(quantity)) {
            responseDTO.setContent("Invalid Quantity");
        } else if (Integer.parseInt(quantity) <= 0) {
            responseDTO.setContent("Quantity must be greater than 0");
        } else if (image1.getSubmittedFileName() == null) {
            responseDTO.setContent("Please select image 1");
        } else if (image2.getSubmittedFileName() == null) {
            responseDTO.setContent("Please select image 2");
        } else if (image3.getSubmittedFileName() == null) {
            responseDTO.setContent("Please select image 3");
        } else {
            System.out.println("2");
            Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));

            if (category == null) {
                responseDTO.setContent("Please Select a Valid Category");
            } else {

                Model model = (Model) session.get(Model.class, Integer.valueOf(modelId));

                if (model == null) {
                    responseDTO.setContent("Please Select a Valid Model");
                } else {

                    if (model.getCategory().getId() != category.getId()) {
                        responseDTO.setContent("Please Select a Valid Model");
                    } else {
                        Storage storage = (Storage) session.get(Storage.class, Integer.valueOf(storageId));

                        if (storage == null) {
                            responseDTO.setContent("Please Select a Valid Storage");
                        } else {
                            Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));

                            if (color == null) {
                                responseDTO.setContent("Please Select a Valid Color");
                            } else {
                                ProductCondition condition = (ProductCondition) session.get(ProductCondition.class, Integer.valueOf(conditionId));

                                if (condition == null) {
                                    responseDTO.setContent("Please Select a Valid Condition");
                                } else {

                                }
                            }
                        }
                    }

                }
            }

//            Criteria criteria1 = session.createCriteria(type);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
        System.out.println(gson.toJson(responseDTO));

        session.close();

    }

}
