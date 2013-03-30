package org.javaswift.joss.command.mock.account;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

import java.util.Collection;

public class AccountMetadataCommandMock extends CommandMock implements AccountMetadataCommand {

    private Collection<? extends Header> headers;

    public AccountMetadataCommandMock(Swift swift, Account account, Collection<? extends Header> headers) {
        super(swift, account);
        this.headers = headers;
    }

    @Override
    public SwiftResult<Object> callSwift() {
        return swift.saveMetadata(headers);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}
