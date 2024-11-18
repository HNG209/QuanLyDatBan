package org.login.quanlydatban.entity.keygenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "daily_customer_counter")
public class DailyCustomerCounter {

    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "countValue")
    private int counterValue;

    public int getCounterDate() {
        return year;
    }

    public void setCounterDate(int year) {
        this.year = year;
    }

    public int getCounterValue() {
        return counterValue;
    }

    public void setCounterValue(int counterValue) {
        this.counterValue = counterValue;
    }
}
