package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.shared.object.DeleteObjectCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.model.StoredObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

public class DeleteObjectCommandImpl extends AbstractObjectCommand<HttpDelete, Object> implements DeleteObjectCommand {

    public DeleteObjectCommandImpl(Account account, HttpClient httpClient, AccessImpl access, StoredObject target) {
        super(account, httpClient, access, target);
    }

    @Override
    protected HttpDelete createRequest(String url) {
        return new HttpDelete(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
