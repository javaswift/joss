package org.javaswift.joss.information;

import org.javaswift.joss.headers.account.AccountBytesUsed;
import org.javaswift.joss.headers.account.AccountContainerCount;
import org.javaswift.joss.headers.account.AccountObjectCount;
import org.javaswift.joss.headers.account.ServerDate;

public class AccountInformation extends AbstractInformation {

    private AccountContainerCount containerCount;

    private AccountObjectCount objectCount;

    private AccountBytesUsed bytesUsed;

    private ServerDate serverDate;

    public int getContainerCount() {
        return Integer.parseInt(containerCount.getHeaderValue());
    }

    public void setContainerCount(AccountContainerCount containerCount) {
        this.containerCount = containerCount;
    }

    public long getBytesUsed() {
        return Long.parseLong(bytesUsed.getHeaderValue());
    }

    public void setBytesUsed(AccountBytesUsed bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public int getObjectCount() {
        return Integer.parseInt(objectCount.getHeaderValue());
    }

    public void setObjectCount(AccountObjectCount objectCount) {
        this.objectCount = objectCount;
    }

    public long getServerDate() {
        return Long.parseLong(serverDate.getHeaderValue());
    }

    public void setServerDate(ServerDate serverDate) {
        this.serverDate = serverDate;
    }

}
