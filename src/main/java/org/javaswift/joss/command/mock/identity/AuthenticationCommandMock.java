package org.javaswift.joss.command.mock.identity;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class AuthenticationCommandMock extends CommandMock<AccessImpl> implements AuthenticationCommand {

    private final String tenant;
    private final String username;
    private final String password;

    public AuthenticationCommandMock(Swift swift, String url, String tenant, String username, String password) {
        super(swift, null, null, null);
        this.tenant = tenant;
        this.username = username;
        this.password = password;
    }

    @Override
    public SwiftResult<AccessImpl> callSwift() {
        return swift.authenticate(tenant, username, password);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

}
