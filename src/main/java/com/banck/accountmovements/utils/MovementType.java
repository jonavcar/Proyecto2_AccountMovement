/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banck.accountmovements.utils;

/**
 *
 * @author jonavcar
 */
public enum MovementType {
    CHARGE("CG") {
        @Override
        public boolean equals(String movementType) {
            return value.equals(movementType);
        }
    },
    PAYMENT("AB") {
        @Override
        public boolean equals(String movementType) {
            return value.equals(movementType);
        }
    };

    public final String value;

    public boolean equals(String movementType) {
        return value.equals(movementType);
    }

    private MovementType(String value) {
        this.value = value;
    }
}
