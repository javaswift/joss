package org.javaswift.joss.client.website;

import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.StoredObject;

public class ObjectStoreFileObject extends AbstractFileObject<LocalFileObject> {

    StoredObject object;

    public ObjectStoreFileObject(StoredObject object) {
        this.object = object;
    }

    @Override
    public void delete() {
        getObject().delete();
    }

    @Override
    public void save(LocalFileObject sourceFile) {
        UploadInstructions uploadInstructions = new UploadInstructions(sourceFile.getFile())
                .setMd5(sourceFile.getMd5());
        getObject().uploadObject(uploadInstructions);
    }

    @Override
    public String getMd5() {
        return getObject().getEtag();
    }

    public StoredObject getObject() {
        return this.object;
    }

}
