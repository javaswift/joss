package org.javaswift.joss.client.core;

import org.javaswift.joss.headers.DateHeader;
import org.javaswift.joss.util.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ServerTime {

    public static final Logger LOG = LoggerFactory.getLogger(ServerTime.class);

    final long modifierInMilliseconds;

    public ServerTime(final long modifierInMilliseconds) {
        this.modifierInMilliseconds = modifierInMilliseconds;
    }

    public long getServerTimeInSeconds() {
        return (LocalTime.currentTime() + this.modifierInMilliseconds) / 1000;
    }

    public long getServerTime(long seconds) {
        return getServerTimeInSeconds() + seconds;
    }

    public static ServerTime create(long serverTimeInMS, long localTimeInMS) {
        LOG.info("JOSS / Server time is "+ DateHeader.convertDateToString(new Date(serverTimeInMS)));
        LOG.info("JOSS / Local time is "+ DateHeader.convertDateToString(new Date(localTimeInMS)));
        long modifierInMilliseconds = serverTimeInMS - localTimeInMS;
        LOG.info("JOSS / Local time modifier in milliseconds: "+ modifierInMilliseconds);
        return new ServerTime(modifierInMilliseconds);
    }

}
