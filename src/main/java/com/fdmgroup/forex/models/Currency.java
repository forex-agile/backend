package com.fdmgroup.forex.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Currency {

    @Id
    private String currencyCode;

    @Column(unique = true, nullable = false)
    private String currencyName;

    public Currency() {}

    public Currency(String code, String name) {
        setCurrencyCode(code);
        setCurrencyName(name);
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String newCode) {
        if (newCode != null && newCode.length() == 3) {
            this.currencyCode = newCode;
        }
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String newName) {
        if (newName != null && newName != "") {
            this.currencyName = newName;
        }
    }

    public String toString() {
        return "Currency [code=" + currencyCode + ", name=" + currencyName + "]";
    }

}
