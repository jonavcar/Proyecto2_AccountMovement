package com.banck.accountmovements.domain;

import lombok.Data;

/**
 *
 * @author jonavcar
 */
@Data
public class Account {

    public String account;
    public String customer;
    public String customerType;
    public String accountType;
    public String dateCreated;
    public double mmpdm;
    public int topMMovem;
    public boolean comMaint;
    public int dayMovem;
    public boolean status;
}
