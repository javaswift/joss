package org.javaswift.joss.command.shared.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.object.*;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.StoredObject;

import java.io.File;
import java.util.Collection;

public interface StoredObjectCommandFactory {
    
    CopyObjectCommand createCopyObjectCommand(Account account, HttpClient httpClient, AccessImpl access,
                                              StoredObject sourceObject, StoredObject targetObject);

    DeleteObjectCommand createDeleteObjectCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                  StoredObject target);

    DownloadObjectAsByteArrayCommand createDownloadObjectAsByteArrayCommand(Account account, HttpClient httpClient,
                                                                            AccessImpl access, StoredObject object,
                                                                            DownloadInstructions downloadInstructions);

    DownloadObjectAsInputStreamCommand createDownloadObjectAsInputStreamCommand(Account account, HttpClient httpClient,
                                                                                AccessImpl access, StoredObject object,
                                                                                DownloadInstructions downloadInstructions);

    DownloadObjectToFileCommand createDownloadObjectToFileCommand(Account account, HttpClient httpClient,
                                                                  AccessImpl access, StoredObject object,
                                                                  DownloadInstructions downloadInstructions, File targetFile);

    ObjectInformationCommand createObjectInformationCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                            StoredObject object);

    ObjectMetadataCommand createObjectMetadataCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                      StoredObject object, Collection<? extends Header> headers);

    UploadObjectCommand createUploadObjectCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                  StoredObject target, UploadInstructions uploadInstructions);

}
