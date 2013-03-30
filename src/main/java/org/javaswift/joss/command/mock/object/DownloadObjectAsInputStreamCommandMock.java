package org.javaswift.joss.command.mock.object;

import org.apache.http.HttpStatus;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusChecker;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusFailCondition;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusMatch;
import org.javaswift.joss.command.impl.core.httpstatus.HttpStatusSuccessCondition;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.command.shared.object.DownloadObjectAsInputStreamCommand;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.javaswift.joss.swift.SwiftResult;

import java.io.InputStream;

public class DownloadObjectAsInputStreamCommandMock extends CommandMock<InputStream> implements DownloadObjectAsInputStreamCommand {

    private DownloadInstructions downloadInstructions;

    public DownloadObjectAsInputStreamCommandMock(Swift swift, Account account, Container container, StoredObject object,
                                                  DownloadInstructions downloadInstructions) {
        super(swift, account, container, object);
        this.downloadInstructions = downloadInstructions;
    }

    @Override
    public SwiftResult<InputStream> callSwift() {
        return swift.downloadObjectAsInputStream(container, object, downloadInstructions);
    }

    @Override
    public HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_OK)),
                new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_PARTIAL_CONTENT)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_MODIFIED)),
                new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_PRECONDITION_FAILED))
        };
    }
}
