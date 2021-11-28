package com.banck.accountmovements.domain;

import lombok.Data;

/**
 *
 * @author jonavcar
 */
@Data
public class Movement {

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
