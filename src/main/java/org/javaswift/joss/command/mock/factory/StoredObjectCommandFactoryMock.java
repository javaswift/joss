package org.javaswift.joss.command.mock.factory;

import java.io.File;
import java.util.Collection;

import org.javaswift.joss.command.mock.object.CopyObjectCommandMock;
import org.javaswift.joss.command.mock.object.DeleteObjectCommandMock;
import org.javaswift.joss.command.mock.object.DownloadObjectAsByteArrayCommandMock;
import org.javaswift.joss.command.mock.object.DownloadObjectAsInputStreamCommandMock;
import org.javaswift.joss.command.mock.object.DownloadObjectToFileCommandMock;
import org.javaswift.joss.command.mock.object.ObjectInformationCommandMock;
import org.javaswift.joss.command.mock.object.ObjectMetadataCommandMock;
import org.javaswift.joss.command.mock.object.UploadObjectCommandMock;
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
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;

public class StoredObjectCommandFactoryMock implements StoredObjectCommandFactory {

    private final Swift swift;

    public StoredObjectCommandFactoryMock(Swift swift) {
        this.swift = swift;
    }

    @Override
    public CopyObjectCommand createCopyObjectCommand(Account account, Container sourceContainer, StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        return new CopyObjectCommandMock(swift, account, sourceContainer, sourceObject, targetContainer, targetObject);
    }

    @Override
    public DeleteObjectCommand createDeleteObjectCommand(Account account, Container container, StoredObject target) {
        return new DeleteObjectCommandMock(swift, account, container, target);
    }

    @Override
    public DownloadObjectAsByteArrayCommand createDownloadObjectAsByteArrayCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsByteArrayCommandMock(swift, account, container, object, downloadInstructions);
    }

    @Override
    public DownloadObjectAsInputStreamCommand createDownloadObjectAsInputStreamCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsInputStreamCommandMock(swift, account, container, object, downloadInstructions);
    }

    @Override
    public DownloadObjectToFileCommand createDownloadObjectToFileCommand(Account account, Container container, StoredObject object, DownloadInstructions downloadInstructions, File targetFile) {
        return new DownloadObjectToFileCommandMock(swift, account, container, object, downloadInstructions, targetFile);
    }

    @Override
    public ObjectInformationCommand createObjectInformationCommand(Account account, Container container, StoredObject object, boolean allowErrorLog) {
        return new ObjectInformationCommandMock(swift, account, container, object, allowErrorLog);
    }

    @Override
    public ObjectMetadataCommand createObjectMetadataCommand(Account account, Container container, StoredObject object, Collection<? extends Header> headers) {
        return new ObjectMetadataCommandMock(swift, account, container, object, headers);
    }

    @Override
    public UploadObjectCommand createUploadObjectCommand(Account account, Container container, StoredObject target, UploadInstructions uploadInstructions) {
        return new UploadObjectCommandMock(swift, account, container, target, uploadInstructions);
    }

    @Override
    public String getTempUrlPrefix() {
        return "";
    }

    @Override
    public Character getDelimiter() {
        return swift.getDelimiter();
    }

}
