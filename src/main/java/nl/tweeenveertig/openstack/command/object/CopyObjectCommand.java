package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.headers.object.CopyFrom;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

public class CopyObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public CopyObjectCommand(Account account, HttpClient httpClient, AccessImpl access, Container sourceContainer,
                             StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        super(account, httpClient, access, targetContainer, targetObject);
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
