/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.google.gson.annotations.Expose;
import entity.Color;
import entity.Model;
import entity.Product_Condition;
import entity.Storage;
import entity.User;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author sande
 */
public class Product_DTO implements Serializable{
    
    @Expose
    private int id;
    
    @Expose
    private Product_Condition condition;
    
    @Expose
    private Model model;

    @Expose
    private String title;

    @Expose
    private String description;

    @Expose
    private String price;

    @Expose
    private String qty;
    
    @Expose
    private Storage storage;
    
    @Expose
    private Color color;
    
    @Expose
    private User user;

    @Expose
    private String date_time;
    
    @Expose
    private String status;
    
    public Product_DTO(){}
    
}
