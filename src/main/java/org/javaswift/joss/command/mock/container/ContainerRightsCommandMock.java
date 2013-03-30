package org.javaswift.joss.command.mock.container;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.container.ContainerRightsCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class ContainerRightsCommandMock extends CommandMock<String[]> implements ContainerRightsCommand {

    private final boolean publicContainer;

    public ContainerRightsCommandMock(Swift swift, Account account, Container container, boolean publicContainer) {
        super(swift, account, container);
        this.publicContainer = publicContainer;
    }

    @Override
    public SwiftResult<String[]> callSwift() {
        return swift.setContainerPrivacy(container.getName(), publicContainer);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}
