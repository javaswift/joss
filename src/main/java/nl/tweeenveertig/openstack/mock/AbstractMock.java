package nl.tweeenveertig.openstack.mock;

import nl.tweeenveertig.openstack.command.core.AbstractInformation;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractMock<I extends AbstractInformation> {

    private Map<String, Object> metadata = new TreeMap<String, Object>();

    public void setInfo(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public I getInfo() {
        I info = createInformationContainer();
        for (String metadataKey : metadata.keySet()) {
            info.addMetadata(metadataKey, metadata.get(metadataKey).toString());
        }
        appendInformation(info);
        return info;
    }

    protected abstract void appendInformation(I info);

    protected abstract I createInformationContainer();

}
