package org.javaswift.joss.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.javaswift.joss.client.core.AbstractClient;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.factory.AuthenticationCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ClientImpl extends AbstractClient<AccountImpl> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private HttpClient httpClient;

    private HttpClientBuilder clientBuilder;

    public ClientImpl(AccountConfig accountConfig) {
        super(accountConfig);
        initHttpClient(accountConfig.getSocketTimeout());
    }

    private void initProxySettings() {
        if (!accountConfig.isUseProxy()) {
            return;
        }

        ProxyConfigurer proxyConfigurer = new ProxyConfigurer(
                accountConfig.getProxyHost(),
                accountConfig.getProxyPort(),
                accountConfig.getProxyUsername(),
                accountConfig.getProxyPassword()
        );

        if (!proxyConfigurer.hasHostAndPort()) {
            LOG.error("JOSS / Invalid proxy settings");
            return;
        }

        proxyConfigurer.configureHttpClientBuilder(clientBuilder);
    }

    private void initHttpClient(int socketTimeout) {
        PoolingHttpClientConnectionManager connectionManager = initConnectionManager();

        clientBuilder = HttpClientBuilder.create();

        initProxySettings();

        if (accountConfig.isDisableSslValidation()) {
            LOG.info("JOSS / Disable SSL verification");
            clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());
        }

        if (socketTimeout != -1) {
            final RequestConfig requestConfig = RequestConfig.custom()
                                                             .setConnectTimeout(socketTimeout)
                                                             .setConnectionRequestTimeout(socketTimeout)
                                                             .setSocketTimeout(socketTimeout).build();

            LOG.info("JOSS / Set socket timeout on HttpClient: " + socketTimeout);

            clientBuilder.setDefaultRequestConfig(requestConfig);
        }

        this.httpClient = clientBuilder.useSystemProperties().setConnectionManager(connectionManager).build();
    }

    protected PoolingHttpClientConnectionManager initConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(25);
        return connectionManager;
    }

    @Override
    protected void logSettings() {
        LOG.info("JOSS / Creating real account instance");
        LOG.info("JOSS / * Allow caching: "+accountConfig.isAllowCaching());
    }

    @Override
    protected AuthenticationCommandFactory createFactory() {
        return new AuthenticationCommandFactoryImpl();
    }

    @Override
    protected AccountImpl createAccount() {
        AuthenticationCommand command = this.factory.createAuthenticationCommand(httpClient, accountConfig);
        LOG.info(
                "JOSS / Attempting authentication with tenant name: " + accountConfig.getTenantName()+
                        ", tenant ID: "+accountConfig.getTenantId()+
                        ", username: " +accountConfig.getUsername()+
                        ", Auth URL: " +accountConfig.getAuthUrl());
        Access access = command.call();
        LOG.info("JOSS / Successfully authenticated");
        access.setPreferredRegion(accountConfig.getPreferredRegion());
        LOG.info("JOSS / Applying preferred region: "+(accountConfig.getPreferredRegion() == null ? "none" : accountConfig.getPreferredRegion()));
        LOG.info("JOSS / Using TempURL hash prefix source: "+accountConfig.getTempUrlHashPrefixSource());
        return new AccountImpl(command, httpClient, access, accountConfig.isAllowCaching(),
                               accountConfig.getTempUrlHashPrefixSource(), accountConfig.getDelimiter());
    }

    public ClientImpl setHttpClient(HttpClient httpClient) {
        if (httpClient != null) {
            LOG.info("JOSS / Use HTTP client set by client (overrides previous HttpClient settings)");
            this.httpClient = httpClient;
        }
        return this;
    }
    
    public static TrustManager[] gullibleManagers = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public void checkClientTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {
            }

            @Override
            public void checkServerTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
    };
    
    public static SSLContext createGullibleSslContext() throws GeneralSecurityException {
        SSLContext ctx = SSLContext.getInstance( "SSL" );
        ctx.init( null, gullibleManagers, new SecureRandom() );
        return ctx;
    }

}
