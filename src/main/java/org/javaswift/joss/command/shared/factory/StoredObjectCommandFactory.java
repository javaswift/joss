package org.javaswift.joss.command.shared.factory;

import org.javaswift.joss.command.shared.object.*;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

import java.io.File;
import java.util.Collection;

public interface StoredObjectCommandFactory {
    
    CopyObjectCommand createCopyObjectCommand(Account account, Container sourceContainer, StoredObject sourceObject,
                                              Container targetContainer, StoredObject targetObject);

    DeleteObjectCommand createDeleteObjectCommand(Account account, Container container,
                                                  StoredObject target);

    DownloadObjectAsByteArrayCommand createDownloadObjectAsByteArrayCommand(Account account, Container container, StoredObject object,
                                                                            DownloadInstructions downloadInstructions);

    DownloadObjectAsInputStreamCommand createDownloadObjectAsInputStreamCommand(Account account, Container container, StoredObject object,
                                                                                DownloadInstructions downloadInstructions);

    DownloadObjectToFileCommand createDownloadObjectToFileCommand(Account account, Container container, StoredObject object,
                                                                  DownloadInstructions downloadInstructions, File targetFile);

    ObjectInformationCommand createObjectInformationCommand(Account account, Container container,
                                                            StoredObject object);

    ObjectMetadataCommand createObjectMetadataCommand(Account account, Container container,
                                                      StoredObject object, Collection<? extends Header> headers);

    UploadObjectCommand createUploadObjectCommand(Account account, Container container,
                                                  StoredObject target, UploadInstructions uploadInstructions);

}
