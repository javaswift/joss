package org.javaswift.joss.command.impl.factory;

import java.io.File;
import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.object.CopyObjectCommandImpl;
import org.javaswift.joss.command.impl.object.DeleteObjectCommandImpl;
import org.javaswift.joss.command.impl.object.DownloadObjectAsByteArrayCommandImpl;
import org.javaswift.joss.command.impl.object.DownloadObjectAsInputStreamCommandImpl;
import org.javaswift.joss.command.impl.object.DownloadObjectToFileCommandImpl;
import org.javaswift.joss.command.impl.object.ObjectInformationCommandImpl;
import org.javaswift.joss.command.impl.object.ObjectMetadataCommandImpl;
import org.javaswift.joss.command.impl.object.UploadObjectCommandImpl;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.command.shared.object.CopyObjectCommand;
import org.javaswift.joss.command.shared.object.DeleteObjectCommand;
import org.javaswift.joss.command.shared.object.DownloadObjectAsByteArrayCommand;
import org.javaswift.joss.command.shared.object.DownloadObjectAsInputStreamCommand;
import org.javaswift.joss.command.shared.object.DownloadObjectToFileCommand;
import org.javaswift.joss.command.shared.object.ObjectInformationCommand;
import org.javaswift.joss.command.shared.object.ObjectMetadataCommand;
import org.javaswift.joss.command.shared.object.UploadObjectCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

public class StoredObjectCommandFactoryImpl implements StoredObjectCommandFactory {

    private ContainerCommandFactoryImpl containerCommandFactory;
    
    public StoredObjectCommandFactoryImpl(ContainerCommandFactoryImpl containerCommandFactory) {
        this.containerCommandFactory = containerCommandFactory;
    }

    @Override
    public CopyObjectCommand createCopyObjectCommand(Account account, Container sourceContainer, StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        return new CopyObjectCommandImpl(account, getHttpClient(), getAccess(), sourceObject, targetObject);
    }

    @Override
    public DeleteObjectCommand createDeleteObjectCommand(Account account, Container container, StoredObject target) {
        return new DeleteObjectCommandImpl(account, getHttpClient(), getAccess(), target);
    }

    @Override
    public DownloadObjectAsByteArrayCommand createDownloadObjectAsByteArrayCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsByteArrayCommandImpl(account, getHttpClient(), getAccess(), object, downloadInstructions);
    }

    @Override
    public DownloadObjectAsInputStreamCommand createDownloadObjectAsInputStreamCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsInputStreamCommandImpl(account, getHttpClient(), getAccess(), object, downloadInstructions);
    }

    @Override
    public DownloadObjectToFileCommand createDownloadObjectToFileCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions, File targetFile) {
        return new DownloadObjectToFileCommandImpl(account, getHttpClient(), getAccess(), object, downloadInstructions, targetFile);
    }

    @Override
    public ObjectInformationCommand createObjectInformationCommand(Account account, Container container, StoredObject object, boolean allowErrorLog) {
        return new ObjectInformationCommandImpl(account, getHttpClient(), getAccess(), object, allowErrorLog);
    }

    @Override
    public ObjectMetadataCommand createObjectMetadataCommand(Account account, Container container, StoredObject object, Collection<? extends Header> headers) {
        return new ObjectMetadataCommandImpl(account, getHttpClient(), getAccess(), object, headers);
    }

    @Override
    public UploadObjectCommand createUploadObjectCommand(Account account, Container container, StoredObject target, UploadInstructions uploadInstructions) {
        return new UploadObjectCommandImpl(account, getHttpClient(), getAccess(), target, uploadInstructions);
    }

    @Override
    public String getTempUrlPrefix() {
        return containerCommandFactory.getTempUrlPrefix();
    }

    public HttpClient getHttpClient() {
        return containerCommandFactory.getHttpClient();
    }

    public Access getAccess() {
        return containerCommandFactory.getAccess();
    }

    public Character getDelimiter() { return containerCommandFactory.getDelimiter(); }
}
