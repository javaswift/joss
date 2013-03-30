package org.javaswift.joss.command.mock.container;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.container.ContainerInformationCommand;
import org.javaswift.joss.information.ContainerInformation;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class ContainerInformationCommandMock extends CommandMock<ContainerInformation> implements ContainerInformationCommand {

    public ContainerInformationCommandMock(Swift swift, Account account, Container container) {
        super(swift, account, container);
    }

    @Override
    public SwiftResult<ContainerInformation> callSwift() {
        return swift.getContainerInformation(container.getName());
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
