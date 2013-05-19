package org.javaswift.joss.command.impl.object;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.params.CoreProtocolPNames;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.shared.object.UploadObjectCommand;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.IOException;

public class UploadObjectCommandImpl extends AbstractObjectCommand<HttpPut, Object> implements UploadObjectCommand {

    public UploadObjectCommandImpl(Account account, HttpClient httpClient, Access access,
                                   StoredObject target, UploadInstructions uploadInstructions) {
        super(account, httpClient, access, target);
        try {
            prepareUpload(uploadInstructions);
        } catch (IOException err) {
            throw new CommandException("Unable to open input stream for uploading", err);
        }
    }

    protected void prepareUpload(UploadInstructions uploadInstructions) throws IOException {
        HttpEntity entity = uploadInstructions.getEntity();
        setHeader(uploadInstructions.getDeleteAt());
        setHeader(uploadInstructions.getDeleteAfter());
        setHeader(uploadInstructions.getObjectManifest());
        setHeader(uploadInstructions.getEtag());
        setHeader(uploadInstructions.getContentType());
        request.setEntity(entity);
    }

    @Override
    protected HttpPut createRequest(String url) {
        HttpPut putMethod = new HttpPut(url);
        putMethod.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        return putMethod;
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_LENGTH_REQUIRED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_UNPROCESSABLE_ENTITY))
        };
    }

}
