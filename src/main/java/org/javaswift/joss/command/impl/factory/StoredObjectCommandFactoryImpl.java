package org.javaswift.joss.command.impl.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.object.*;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.object.*;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.File;
import java.util.Collection;

public class StoredObjectCommandFactoryImpl implements StoredObjectCommandFactory {
    @Override
    public CopyObjectCommand createCopyObjectCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject sourceObject, StoredObject targetObject) {
        return new CopyObjectCommandImpl(account, httpClient, access, sourceObject, targetObject);
    }

    @Override
    public DeleteObjectCommand createDeleteObjectCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject target) {
        return new DeleteObjectCommandImpl(account, httpClient, access, target);
    }

    @Override
    public DownloadObjectAsByteArrayCommand createDownloadObjectAsByteArrayCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsByteArrayCommandImpl(account, httpClient, access, object, downloadInstructions);
    }

    @Override
    public DownloadObjectAsInputStreamCommand createDownloadObjectAsInputStreamCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsInputStreamCommandImpl(account, httpClient, access, object, downloadInstructions);
    }

    @Override
    public DownloadObjectToFileCommand createDownloadObjectToFileCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object, DownloadInstructions downloadInstructions, File targetFile) {
        return new DownloadObjectToFileCommandImpl(account, httpClient, access, object, downloadInstructions, targetFile);
    }

    @Override
    public ObjectInformationCommand createObjectInformationCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object) {
        return new ObjectInformationCommandImpl(account, httpClient, access, object);
    }

    @Override
    public ObjectMetadataCommand createObjectMetadataCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject object, Collection<? extends Header> headers) {
        return new ObjectMetadataCommandImpl(account, httpClient, access, object, headers);
    }

    @Override
    public UploadObjectCommand createUploadObjectCommand(Account account, HttpClient httpClient, AccessImpl access, StoredObject target, UploadInstructions uploadInstructions) {
        return new UploadObjectCommandImpl(account, httpClient, access, target, uploadInstructions);
    }
}
