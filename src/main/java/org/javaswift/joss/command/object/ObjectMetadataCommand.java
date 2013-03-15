package org.javaswift.joss.command.object;

import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.headers.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.Collection;

public class ObjectMetadataCommand extends AbstractObjectCommand<HttpPost, Object> {

    public ObjectMetadataCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object, Collection<? extends Header> headers) {
        super(account, httpClient, access, object);
        addHeaders(headers);
    }

    @Override
    protected HttpPost createRequest(String url) {
        return new HttpPost(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_ACCEPTED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
