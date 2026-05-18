package isp.entity;

import java.io.Serializable;


import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.Table;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;


@Entity
@NamedQueries({
    @NamedQuery(
        name = "Transaction.findByAccount",
        query = "SELECT t FROM Transaction t WHERE t.account = :account ORDER BY t.transactionDate DESC"
    ),
    @NamedQuery(
        name = "Transaction.findByUserId",
        query = "SELECT t FROM Transaction t WHERE t.account.user.userId = :userId ORDER BY t.transactionDate DESC"
    )
})

@Table(name = "[Transaction]", schema = "dbo")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private int transactionId;
    private Account account;  
    private Category category; 
    private Timestamp transactionDate;
    private double amount;
    private String note;
    private boolean repeatingTransaction;
    

    public Transaction() {
    }

    public Transaction(Account account, Category category, Timestamp transactionDate, double amount, String note, boolean repeatingTransaction) 
    {
        this.account = account;
        this.category = category;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.note = note;
        this.repeatingTransaction = repeatingTransaction;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionId")
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @ManyToOne
    @JoinColumn(name = "AccountId", referencedColumnName = "AccountId")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @ManyToOne
    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(name = "TransactionDate")
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Column(name = "Amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double newAmount) {
        this.amount = newAmount;
    }

    @Column(name = "Note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    @Column(name = "RepeatingTransaction")
    public boolean isRepeatingTransaction() {
        return repeatingTransaction;
    }
    public void setRepeatingTransaction(boolean repeatingTransaction) {
        this.repeatingTransaction = repeatingTransaction;
    }
    

}
