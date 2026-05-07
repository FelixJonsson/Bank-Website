package isp.eao;

import jakarta.ejb.Local;
import isp.entity.User;
import isp.entity.Account;

@Local
public interface AccountEAOLocal {
    public Account findAccount(int id);
    public Account createAccount(Account account);
    public Account updateAccount(Account account);
    public void deleteAccount(int id);
    public Account findAccountByUser(User user);

}