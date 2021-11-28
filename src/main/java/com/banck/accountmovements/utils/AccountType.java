/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.accountmovements.utils;

/**
 *
 * @author jonavcar
 */
public enum AccountType {
    SAVINGS_ACCOUNT("CA") {
        @Override
        public boolean equals(String customerType) {
            return value.equals(customerType);
        }
    },
    CURRENT_ACCOUNT("CC") {
        @Override
        public boolean equals(String customerType) {
            return value.equals(customerType);
        }
    }, FIXED_TERM_ACCOUNT("CF") {
        @Override
        public boolean equals(String customerType) {
            return value.equals(customerType);
        }
    };

    public final String value;

    public boolean equals(String customerType) {
        return value.equals(customerType);
    }

    private AccountType(String value) {
        this.value = value;
    }
}
