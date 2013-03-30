package org.javaswift.joss.command.mock.account;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

import java.util.Collection;

public class ListContainersCommandMock extends CommandMock<Collection<Container>> implements ListContainersCommand {

    private ListInstructions listInstructions;

    public ListContainersCommandMock(Swift swift, Account account, ListInstructions listInstructions) {
        super(swift, account);
        this.listInstructions = listInstructions;
    }

    @Override
    public SwiftResult<Collection<Container>> callSwift() {
        return swift.listContainers(account, listInstructions);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT))
        };
    }

}
