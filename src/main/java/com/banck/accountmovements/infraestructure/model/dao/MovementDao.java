package com.banck.accountmovements.infraestructure.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jonavcar
 */
@Data
@Document("movement")
public class MovementDao {

    @Id
    public String movement;
    public String movementType;
    public String account;
    public String customer;
     public String transferAccount;
    public String transferCustomer;
    public double amount;
    public String concept;
    public String observations;
    public String date;
    public String time;
    public boolean correct;
}
