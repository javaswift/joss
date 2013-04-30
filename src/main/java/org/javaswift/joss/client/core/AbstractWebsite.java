package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Website;

import java.io.File;

public abstract class AbstractWebsite extends AbstractContainer implements Website {

    public AbstractWebsite(Account account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    @Override
    public String getIndexPage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getErrorPage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setIndexPage(String indexPage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setErrorPage(String errorPage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pushDirectory(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pullDirectory(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
