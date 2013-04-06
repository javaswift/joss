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
    protected boolean allowErrorLog = true;

    protected Swift swift;

    public CommandMock(Swift swift, Account account) {
        this(swift, account, null, null);
    }

    public CommandMock(Swift swift, Account account, Container container) {
        this(swift, account, container, null);
    }

    public CommandMock(Swift swift, Account account, Container container, StoredObject object) {
        this.swift = swift;
        this.account = account;
        this.container = container;
        this.object = object;
    }

    public void setAllowErrorLog(boolean allowErrorLog) {
        this.allowErrorLog = allowErrorLog;
    }

    protected void applyDelay() {
        if (swift.getMillisDelay() > 0) {
            try {
                Thread.sleep(swift.getMillisDelay());
            } catch (InterruptedException e) {
                throw new CommandException("Sleep interrupted", e);
            }
        }
    }

    public T call() {
        // Custom delay for mock command calls
        applyDelay();
        try {
            // Make sure to check if the command must return a prepared HTTP status code
            int suggestedStatus = swift.getStatus(getClass());
            final SwiftResult<T> result;
            if (suggestedStatus == -1) {
                // The -1 code means that the call is executed as normal
                result = callSwift();
            } else {
                result = new SwiftResult<T>(suggestedStatus);
            }
            // The HTTP status code is checked against the mappers for the command class
            HttpStatusChecker.verifyCode(getStatusCheckers(), result.getStatus());
            return result.getPayload();
        } catch (CommandException err) {
            if (allowErrorLog) {
                LOG.error("JOSS / "+this.getClass().getSimpleName()+
                        ", HTTP status "+err.getHttpStatusCode());
            }
            throw err;
        }
    }

    public abstract SwiftResult<T> callSwift();

    public abstract HttpStatusChecker[] getStatusCheckers();
}
