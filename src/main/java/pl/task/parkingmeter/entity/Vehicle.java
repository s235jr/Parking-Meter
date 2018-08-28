package pl.task.parkingmeter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String regNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime runDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime payDate;
    private BigDecimal bill;
    private boolean ownerDisabled;
    private boolean paid;

    public Vehicle (String regNumber) {
        this.regNumber = regNumber;
    }

    public Vehicle() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public LocalDateTime getRunDate() {
        return runDate;
    }

    public void setRunDate(LocalDateTime runDate) {
        this.runDate = runDate;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public boolean isOwnerDisabled() {
        return ownerDisabled;
    }

    public void setOwnerDisabled(boolean ownerDisabled) {
        this.ownerDisabled = ownerDisabled;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setIsPaid(boolean idPaid) {
        this.paid = idPaid;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", regNumber='" + regNumber + '\'' +
                ", runDate=" + runDate +
                ", payDate=" + payDate +
                ", bill=" + bill +
                ", ownerDisabled=" + ownerDisabled +
                ", paid=" + paid +
                '}';
    }
}
