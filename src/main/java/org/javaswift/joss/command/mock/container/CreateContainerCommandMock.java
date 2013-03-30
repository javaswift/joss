package org.javaswift.joss.command.mock.container;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.container.CreateContainerCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class CreateContainerCommandMock extends CommandMock implements CreateContainerCommand {

    public CreateContainerCommandMock(Swift swift, Account account, Container container) {
        super(swift, account, container);
    }

    @Override
    public SwiftResult callSwift() {
        return swift.createContainer(container.getName());
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED))
        };
    }

}
