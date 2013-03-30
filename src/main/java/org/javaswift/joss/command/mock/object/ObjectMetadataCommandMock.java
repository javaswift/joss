package org.javaswift.joss.command.mock.object;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.object.ObjectMetadataCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

import java.util.Collection;

public class ObjectMetadataCommandMock extends CommandMock implements ObjectMetadataCommand {

    private final Collection<? extends Header> headers;

    public ObjectMetadataCommandMock(Swift swift, Account account, Container container, StoredObject object, Collection<? extends Header> headers) {
        super(swift, account, container, object);
        this.headers = headers;
    }

    @Override
    public SwiftResult callSwift() {
        return swift.saveObjectMetadata(container, object, headers);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
