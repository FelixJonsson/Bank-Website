package isp.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;


@Entity
@NamedQueries({
    @NamedQuery(
        name = "Account.findByUser",
        query = "SELECT a FROM Account a WHERE a.user = :user"
    )
})

@Table(name = "Account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private int accountId;
    private User user; // Relationship to User
    private String accountName;
    private String accountType;
    private BigDecimal currentBalance;

    public Account() {
    }

    public Account(User user, String accountName, String accountType, BigDecimal currentBalance) {
        this.user = user;
        this.accountName = accountName;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountId")
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "AccountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(name = "AccountType")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "CurrentBalance")
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}