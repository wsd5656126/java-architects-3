package com.wusd.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Component
@Scope("prototype") //多个实例,解决线程安全问题
public class TransactionUtils {
    private TransactionStatus transactionStatus;
    @Autowired
    DataSourceTransactionManager transactionManager;

    public TransactionStatus begin() {
        System.out.println("事务开始");
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionAttribute());
        this.transactionStatus = transactionStatus;
        return transactionStatus;
    }

    public void commit(TransactionStatus transactionStatus) {
        System.out.println("事务提交");
        transactionManager.commit(transactionStatus);
    }

    public void rollback() {
        if (transactionStatus != null)
            transactionManager.rollback(transactionStatus);
    }
}
