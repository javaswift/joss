package org.javaswift.joss.client.impl;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyConfigurer {

    public static final Logger LOG = LoggerFactory.getLogger(ProxyConfigurer.class);

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public ProxyConfigurer(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        LOG.info("JOSS / Use proxy: " + host + ":" + port);
    }

    public boolean hasHostAndPort() {
        return host != null && port > 0;
    }

    public void configureHttpClientBuilder(HttpClientBuilder clientBuilder) {
        try {
            if (mustAuthorize()) {
                LOG.info("JOSS / Proxy with authorization: " + username + " \\ *********");
                clientBuilder.setDefaultCredentialsProvider(createCredentialsProvider());
            }
            clientBuilder.setProxy(createProxy());
        } catch (Exception e) {
            LOG.error("JOSS / Invalid proxy authorization settings", e);
        }
    }

    private HttpHost createProxy() {
        return new HttpHost(host, port, getHttpScheme());
    }

    private boolean mustAuthorize() {
        return username != null && password != null;
    }

    private CredentialsProvider createCredentialsProvider() {
        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(username, password));
        return credsProvider;
    }

    private String getHttpScheme() {
        return host.startsWith("https") ? "https" : "http";
    }

}
