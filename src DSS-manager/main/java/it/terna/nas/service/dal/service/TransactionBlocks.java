package it.terna.nas.service.dal.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransactionBlocks
{
    private final TransactionTemplate requiresNewTx;

    public TransactionBlocks(PlatformTransactionManager transactionManager)
    {
        this.requiresNewTx = new TransactionTemplate(transactionManager);
        this.requiresNewTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void requiresNew(Runnable runnable)
    {
        requiresNewTx.executeWithoutResult(status -> {
            runnable.run();
        });
    }

    public <T> T requiresNew(java.util.function.Supplier<T> supplier)
    {
        return requiresNewTx.execute(status -> supplier.get());
    }
}
