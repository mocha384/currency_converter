package com.rakeshgohel.currencyconverter.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by rgohel on 2017-03-25.
 */

@Entity(indexes = {@Index(value = "name ASC")})
public class Currency {
    @Id
    public Long id;

    public String name;
    public Double value;
    public Integer decimal;
    @Generated(hash = 1672835745)
    public Currency(Long id, String name, Double value, Integer decimal) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.decimal = decimal;
    }
    @Generated(hash = 1387171739)
    public Currency() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getValue() {
        return this.value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public Integer getDecimal() {
        return this.decimal;
    }
    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }
}
