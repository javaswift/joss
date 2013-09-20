package org.javaswift.joss.client.impl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.javaswift.joss.client.core.AbstractClient;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.factory.AuthenticationCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientImpl extends AbstractClient<AccountImpl> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private HttpClient httpClient;

    public ClientImpl(AccountConfig accountConfig) {
        super(accountConfig);
        initHttpClient(accountConfig.getSocketTimeout());
    }

    private void initHttpClient(int socketTimeout) {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(25);
        
        if(accountConfig.isDisableSslValidation()) {
            try {
                connectionManager.getSchemeRegistry().register(
                        new Scheme("https", 443,
                                new SSLSocketFactory(createGullibleSslContext(),
                                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
            } catch (GeneralSecurityException e) {
                throw new RuntimeException("Could not initialize SSL Context: " + e, e);
            }
        }
        
        this.httpClient = new DefaultHttpClient(connectionManager);
        if (socketTimeout != -1) {
            LOG.info("JOSS / Set socket timeout on HttpClient: "+socketTimeout);
            HttpParams params = this.httpClient.getParams();
            HttpConnectionParams.setSoTimeout(params, socketTimeout);
        }
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
        AuthenticationCommand command = this.factory.createAuthenticationCommand(
                httpClient,
                accountConfig.getAuthUrl(),
                accountConfig.getTenantName(),
                accountConfig.getTenantId(),
                accountConfig.getUsername(),
                accountConfig.getPassword());
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
        return new AccountImpl(command, httpClient, access, accountConfig.isAllowCaching(), accountConfig.getTempUrlHashPrefixSource());
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
    
    public static HostnameVerifier gullibleVerifier = new HostnameVerifier() {
        @Override
        public boolean verify( String s, SSLSession sslSession ) {
            return true;
        }
    };
    
    public static SSLContext createGullibleSslContext() throws GeneralSecurityException {
        SSLContext ctx = SSLContext.getInstance( "SSL" );
        ctx.init( null, gullibleManagers, new SecureRandom() );
        return ctx;
    }


}
