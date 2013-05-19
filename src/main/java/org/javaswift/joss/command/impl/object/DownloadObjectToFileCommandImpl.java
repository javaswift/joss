package org.javaswift.joss.command.impl.object;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.javaswift.joss.command.shared.object.DownloadObjectToFileCommand;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.util.FileAction;

import java.io.File;
import java.io.IOException;

public class DownloadObjectToFileCommandImpl extends AbstractDownloadObjectCommand<HttpGet, Object> implements DownloadObjectToFileCommand {

    private File targetFile;

    public DownloadObjectToFileCommandImpl(Account account, HttpClient httpClient, Access access,
                                           StoredObject object, DownloadInstructions downloadInstructions, File targetFile) {
        super(account, httpClient, access, object, downloadInstructions);
        this.targetFile = targetFile;
    }

    @Override
    protected void handleEntity(HttpEntity entity) throws IOException {
        FileAction.handleEntity(targetFile, entity);
    }

    @Override
    protected String getMd5() throws IOException {
        return FileAction.getMd5(targetFile);
    }

    @Override
    protected Object getObjectAsReturnObject() {
        return null;
    }

}
