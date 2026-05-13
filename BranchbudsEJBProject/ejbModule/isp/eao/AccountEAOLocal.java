package isp.eao;

import jakarta.ejb.Local;
import java.util.List;
import isp.entity.Account;
import isp.entity.User;

@Local
public interface AccountEAOLocal {
    public Account findAccount(int id);
    public Account createAccount(Account account);
    public Account updateAccount(Account account);
    public void deleteAccount(int id);
    public List<Account> findAllAccounts();
    public Account findAccountByName(String accountName);
    public Account findAccountByUser(User user);
    public Account findAccountByUserId(int userId);
}
