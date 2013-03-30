package org.javaswift.joss.command.mock.container;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.container.ContainerMetadataCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

import java.util.Collection;

public class ContainerMetadataCommandMock extends CommandMock implements ContainerMetadataCommand {

    private final Collection<? extends Header> headers;

    public ContainerMetadataCommandMock(Swift swift, Account account, Container container, Collection<? extends Header> headers) {
        super(swift, account, container);
        this.headers = headers;
    }

    @Override
    public SwiftResult<Object> callSwift() {
        return swift.saveContainerMetadata(container.getName(), headers);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }
}
