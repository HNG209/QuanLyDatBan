package org.login.quanlydatban.entity.keygenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "daily_counter")
public class DailyCounter {

    @Id
    @Column(name = "counter_date")
    private LocalDate counterDate;

    @Column(name = "counter_value")
    private int counterValue;

    // Getters and setters
    public LocalDate getCounterDate() {
        return counterDate;
    }

    public void setCounterDate(LocalDate counterDate) {
        this.counterDate = counterDate;
    }

    public int getCounterValue() {
        return counterValue;
    }

    public void setCounterValue(int counterValue) {
        this.counterValue = counterValue;
    }
}