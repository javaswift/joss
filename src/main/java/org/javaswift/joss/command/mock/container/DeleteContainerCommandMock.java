package org.javaswift.joss.command.mock.container;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.container.DeleteContainerCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class DeleteContainerCommandMock extends CommandMock<String[]> implements DeleteContainerCommand {

    public DeleteContainerCommandMock(Swift swift, Account account, Container container) {
        super(swift, account, container);
    }

    @Override
    public SwiftResult<String[]> callSwift() {
        return swift.deleteContainer(container.getName());
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_CONFLICT))
        };
    }
}
