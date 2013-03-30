package org.javaswift.joss.command.mock.object;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.object.CopyObjectCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class CopyObjectCommandMock extends CommandMock implements CopyObjectCommand {

    private final Container sourceContainer;
    private final StoredObject sourceObject;

    public CopyObjectCommandMock(Swift swift, Account account, Container sourceContainer, StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        super(swift, account, targetContainer, targetObject);
        this.sourceContainer = sourceContainer;
        this.sourceObject = sourceObject;
    }

    @Override
    public SwiftResult callSwift() {
        return swift.copyObject(sourceContainer, sourceObject, container, object);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
