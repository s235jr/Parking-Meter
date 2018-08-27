package pl.task.parkingmeter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "regnumber")
    private String regNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "createddate")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "paydate")
    private LocalDateTime payDate;
    private BigDecimal bill;
    @Column(name = "disabledowner")
    private boolean isOwnerDisabled;
    @Column(name = "paid")
    private boolean isPaid;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
        return isOwnerDisabled;
    }

    public void setOwnerDisabled(boolean ownerDisabled) {
        isOwnerDisabled = ownerDisabled;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean idPaid) {
        this.isPaid = idPaid;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", regNumber='" + regNumber + '\'' +
                ", createdDate=" + createdDate +
                ", payDate=" + payDate +
                ", bill=" + bill +
                ", isOwnerDisabled=" + isOwnerDisabled +
                ", isPaid=" + isPaid +
                '}';
    }
}
