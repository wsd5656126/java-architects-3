package com.wusd.service;

import com.wusd.core.annotation.ExtTransaction;
import com.wusd.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//事务提交注解
//@Transactional
@Service
public class UserService {
    @Autowired
    UserDao userDao;

//    @ExtTransaction
    public void add() {
        userDao.add();
    }
}
