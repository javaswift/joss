package org.javaswift.joss.command.mock.account;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class AccountInformationCommandMock extends CommandMock<AccountInformation> implements AccountInformationCommand {

    public AccountInformationCommandMock(Swift swift, Account account) {
        super(swift, account);
    }

    @Override
    public SwiftResult<AccountInformation> callSwift() {
        return swift.getAccountInformation();
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}
