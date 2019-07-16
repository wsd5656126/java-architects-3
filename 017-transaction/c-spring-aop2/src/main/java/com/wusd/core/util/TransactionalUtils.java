package com.wusd.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Component
public class TransactionalUtils {
    @Autowired
    DataSourceTransactionManager transactionManager;

    //开始事务
    public TransactionStatus begin() {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionAttribute());
        return transactionStatus;
    }

    //事务提交
    public void commit(TransactionStatus transactionStatus) {
        transactionManager.commit(transactionStatus);
    }

    //事务回滚
    public void rollback(TransactionStatus transactionStatus) {
        transactionManager.rollback(transactionStatus);
    }
}
