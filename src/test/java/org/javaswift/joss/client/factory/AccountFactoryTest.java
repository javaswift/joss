package org.javaswift.joss.client.factory;

import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.client.impl.ClientImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AccountFactory.class)
public class AccountFactoryTest {

    @Mock
    private ClientImpl mockedClient;

    @Mock
    private AccountImpl mockedAccount;

    @Before
    public void setup() throws Exception {
        whenNew(ClientImpl.class).withNoArguments().thenReturn(mockedClient);
        when(mockedClient.authenticate(anyString(), anyString(), anyString(), anyString())).thenReturn(mockedAccount);
        when(mockedAccount.setAllowReauthenticate(anyBoolean())).thenReturn(mockedAccount);
    }

    @Test
    public void constructMock() {
        AccountFactory factory = new AccountFactory();
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        factory.setConfig(config);
        assertNotNull(factory.createAccount());
    }

    @Test
    public void constructImpl() throws Exception {
        AccountFactory factory = construct(null);
        assertNotNull(factory.createAccount());
        verify(mockedClient, never()).setHttpClient(null);
    }

    @Test
    public void constructImplWithCustomHttpClient() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        AccountFactory factory = construct(httpClient);
        assertNotNull(factory.createAccount());
        verify(mockedClient, times(1)).setHttpClient(httpClient);
    }

    @Test
    public void constructImplDisallowingReauthenticate() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        AccountFactory factory = construct(httpClient);
        factory.setAllowReauthenticate(false);
        assertNotNull(factory.createAccount());
        verify(mockedAccount, times(1)).setAllowReauthenticate(false);
    }

    @Test
    public void constructImplDisallowingCaching() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        AccountFactory factory = construct(httpClient);
        factory.setAllowCaching(false);
        assertNotNull(factory.createAccount());
        verify(mockedClient, times(1)).setAllowCaching(false);
    }

    @Test
    public void getPublicUrl() {
        AccountFactory factory = new AccountFactory();
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        config.setMockPublicUrl("http://find.me");
        factory.setConfig(config);
        assertEquals("http://find.me", factory.createAccount().getPublicURL());
    }

    protected AccountFactory construct(HttpClient httpClient) throws Exception {
        AccountFactory factory = new AccountFactory();
        AccountConfig config = new AccountConfig();
        config.setAuthUrl(null);
        config.setPassword(null);
        config.setTenant(null);
        config.setUsername(null);
        factory.setConfig(config);
        factory.setHttpClient(httpClient);
        return factory;
    }

}
