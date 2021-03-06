package ltd.inmind.accelerator.service;

import ltd.inmind.accelerator.model.po.UserAccount;

public interface IUserAccountService {

    UserAccount getByAccount(String account);

    /**
     * 注册用户
     * @param userAccount
     */
    void register(UserAccount userAccount);

    Boolean verifyPassword(String account, String password);

    void changePassword(String account, String password);

    void updateUserAccount(UserAccount userAccount);
}
