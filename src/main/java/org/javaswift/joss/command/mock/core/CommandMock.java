package org.javaswift.joss.command.mock.core;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommandMock<T> {

    public static final Logger LOG = LoggerFactory.getLogger(CommandMock.class);

    protected Account account;
    protected Container container;
    protected StoredObject object;

    protected Swift swift;

    public CommandMock(Swift swift, Account account) {
        this(swift, account, null, null);
    }

    public CommandMock(Swift swift, Account account, Container container) {
        this(swift, account, container, null);
    }

    public CommandMock(Swift swift, Account account, StoredObject object) {
        this(swift, account, null, object);
    }

    public CommandMock(Swift swift, Account account, Container container, StoredObject object) {
        this.swift = swift;
        this.account = account;
        this.container = container;
        this.object = object;
    }

    public T call() {
        try {
            SwiftResult<T> result = callSwift();
            HttpStatusChecker.verifyCode(getStatusCheckers(), result.getStatus());
            return result.getPayload();
        } catch (CommandException err) {
            LOG.error(
                    "JOSS / "+this.getClass().getSimpleName()+
                            (err.getHttpStatusCode() == 0 ? "" : ", HTTP status "+err.getHttpStatusCode())+
                            (err.getError() == null ? "" : ", Error "+err.getError())+
                            (err.getMessage() == null ? "" : ", Message '"+err.getMessage()+"'")+
                            (err.getCause() == null ? "" : ", Cause "+err.getCause().getClass().getSimpleName()));
            throw err;
        }
    }

    public abstract SwiftResult<T> callSwift();

    public abstract HttpStatusChecker[] getStatusCheckers();
}
