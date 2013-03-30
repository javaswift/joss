package org.javaswift.joss.command.mock.object;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.*;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.object.ObjectInformationCommand;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class ObjectInformationCommandMock extends CommandMock<ObjectInformation> implements ObjectInformationCommand {

    public ObjectInformationCommandMock(Swift swift, Account account, Container container, StoredObject object) {
        super(swift, account, container, object);
    }

    @Override
    public SwiftResult<ObjectInformation> callSwift() {
        return swift.getObjectInformation(container, object);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
