package org.javaswift.joss.command.mock.account;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.account.TenantCommand;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class TenantCommandMock extends CommandMock<Tenants> implements TenantCommand {
    public TenantCommandMock(Swift swift, Account account) {
        super(swift, account);
    }

    @Override
    public SwiftResult<Tenants> callSwift() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

}
