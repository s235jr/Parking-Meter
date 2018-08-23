package pl.task.parkingmeter.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "regnumber")
    private String regNumber;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "paydate")
    private Timestamp payDate;
    private BigDecimal bill;
    @Column(name="disabledowner")
    private boolean isOwnerDisabled;
    @Column(name = "paid")
    private boolean isPaid;

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

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
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
                ", isPaid=" + isPaid +
                '}';
    }
}
