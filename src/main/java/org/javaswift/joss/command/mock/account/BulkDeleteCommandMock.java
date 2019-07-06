package org.javaswift.joss.command.mock.account;

import java.util.Collection;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.BulkDeleteCommand;
import org.javaswift.joss.command.shared.identity.bulkdelete.BulkDeleteResponse;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.ObjectIdentifier;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class BulkDeleteCommandMock extends CommandMock<BulkDeleteResponse> implements
    BulkDeleteCommand {

    private Collection<ObjectIdentifier> objectsToDelete;

    public BulkDeleteCommandMock(Swift swift, Account account, Collection<ObjectIdentifier> objectsToDelete) {
        super(swift, account);
        this.objectsToDelete = objectsToDelete;
    }

    @Override
    public SwiftResult<BulkDeleteResponse> callSwift() {
        return swift.bulkDelete(this.objectsToDelete);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))};
    }

}
