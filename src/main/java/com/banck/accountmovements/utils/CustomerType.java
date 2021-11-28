/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.accountmovements.utils;

/**
 *
 * @author jonavcar
 */
public enum CustomerType {
    LEGAL_PERSON("PJ") {
        @Override
        public boolean equals(String customerType) {
            return value.equals(customerType);
        }
    },
    NATURAL_PERSON("PN") {
        @Override
        public boolean equals(String customerType) {
            return value.equals(customerType);
        }
    };

    public final String value;

    public boolean equals(String customerType) {
        return value.equals(customerType);
    }

    private CustomerType(String value) {
        this.value = value;
    }
}
