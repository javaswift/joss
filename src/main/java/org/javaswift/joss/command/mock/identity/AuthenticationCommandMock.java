package org.javaswift.joss.command.mock.identity;

import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusRange;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

public class AuthenticationCommandMock extends CommandMock<Access> implements AuthenticationCommand {

    private final String tenantName;
    private final String tenantId;
    private final String username;
    private final String password;

    public AuthenticationCommandMock(Swift swift, String url, String tenantName, String tenantId, String username, String password) {
        super(swift, null, null, null);
        this.tenantName = tenantName;
        this.tenantId = tenantId;
        this.username = username;
        this.password = password;
    }

    @Override
    public SwiftResult<Access> callSwift() {
        return swift.authenticate(tenantName, tenantId, username, password);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusRange(200, 299))
        };
    }

    @Override
    public String getUrl() {
        return null;
    }

}
