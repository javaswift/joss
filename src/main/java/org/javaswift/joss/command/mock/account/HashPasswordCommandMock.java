package org.javaswift.joss.command.mock.account;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.HashPasswordCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class HashPasswordCommandMock extends CommandMock implements HashPasswordCommand {

    private String password;

    public HashPasswordCommandMock(Swift swift, Account account, String password) {
        super(swift, account);
        this.password = password;
    }

    @Override
    public SwiftResult callSwift() {
        return this.swift.saveHashPassword(this.password);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

}
