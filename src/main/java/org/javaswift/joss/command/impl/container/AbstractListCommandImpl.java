package org.javaswift.joss.command.impl.container;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.container.StoredObjectListElement;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

import java.io.IOException;

public abstract class AbstractListCommandImpl<N> extends AbstractContainerCommand<HttpGet, N> {

    protected Container container;

    public AbstractListCommandImpl(Account account, HttpClient httpClient, Access access, Container container, ListInstructions listInstructions) {
        super(account, httpClient, access, container);
        this.container = container;
        modifyURI(listInstructions.getQueryParameters());
    }

    @Override
    protected abstract N getReturnObject(HttpResponse response) throws IOException;

    @Override
    protected HttpGet createRequest(String url) {
        return new HttpGet(url);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_NO_CONTENT)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND))
        };
    }

    protected StoredObject getStoredObject(StoredObjectListElement header) {
        StoredObject object = container.getObject(header.name);
        object.setContentLength(header.bytes);
        object.setContentTypeWithoutSaving(header.contentType);
        object.setEtag(header.hash);
        object.setLastModified(header.lastModified);
        object.metadataSetFromHeaders();
        return object;
    }

}
