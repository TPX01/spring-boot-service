package io.tp.services;

import io.tp.services.exceptions.BusinessException;
import io.tp.services.exceptions.ErrorCode;
import io.tp.services.mapper.AccountMapper;
import io.tp.services.model.Account;
import io.tp.services.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public AccountService() {
    }

    @Transactional(readOnly = true)
    public List<io.tp.rest.model.Account> getAccounts() {
        final List<Account> accountModels = accountRepository.findAll();
        return accountMapper.map(accountModels);
    }

    public io.tp.rest.model.Account get(String id) {
        Account accountModel = accountRepository.findById(id);
        return accountMapper.map(accountModel);
    }

    public String create(io.tp.rest.model.Account account) {
        if (!isValidCreateAccountCommand(account)) {
            throw new BusinessException(ErrorCode.MISSING_CREATE_ACCOUNT_INFORMATION);
        }
        Account accountModel = accountMapper.map(account);
        accountRepository.save(accountModel);
        return accountModel.getId();
    }

    private boolean isValidCreateAccountCommand(@RequestBody io.tp.rest.model.Account account) {
        List<Boolean> validationList = new ArrayList<>();
        validationList.add(account.getType() != null);
        validationList.add(!CollectionUtils.isEmpty(account.getOwners()));
        validationList.add(!StringUtils.isEmpty(account.getIban()));
        if (account.getMoneyAmount() != null) {
            validationList.add(account.getMoneyAmount().getAmount() != null);
            validationList.add(!StringUtils.isEmpty(account.getMoneyAmount().getCurrency()));
        }
        return !validationList.contains(false);
    }

    public void delete(String id) {
        Account account = accountRepository.findById(id);
        accountRepository.delete(account);
    }
}