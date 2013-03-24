package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.object.CopyObjectCommand;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.headers.object.CopyFrom;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CopyObjectCommandImpl extends AbstractObjectCommand<HttpPut, Object> implements CopyObjectCommand {

    public CopyObjectCommandImpl(Account account, HttpClient httpClient, AccessImpl access,
                                 StoredObject sourceObject, StoredObject targetObject) {
        super(account, httpClient, access, targetObject);
        setHeader(new CopyFrom(getObjectPath(sourceObject)));
    }

    @Override
    protected HttpPut createRequest(String url) {
        return new HttpPut(url);
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

}
