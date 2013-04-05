package org.javaswift.joss.client.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.client.impl.ClientImpl;
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
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AccountFactory.class)
public class AccountFactoryTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private ClientImpl mockedClient;

    @Mock
    private AccountImpl mockedAccount;

    @Before
    public void setup() throws Exception {
        whenNew(ClientImpl.class).withNoArguments().thenReturn(mockedClient);
        when(mockedClient.setHttpClient(httpClient)).thenReturn(mockedClient);
        when(mockedClient.setHost(anyString())).thenReturn(mockedClient);
        when(mockedClient.setAllowCaching(true)).thenReturn(mockedClient);
        when(mockedClient.authenticate(anyString(), anyString(), anyString(), anyString())).thenReturn(mockedAccount);
        when(mockedAccount.setAllowReauthenticate(anyBoolean())).thenReturn(mockedAccount);
    }

    @Test
    public void constructMock() {
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        AccountFactory factory = new AccountFactory(config);
        assertNotNull(factory.createAccount());
    }

    @Test
    public void getPublicUrl() {
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        config.setHost("http://find.me");
        AccountFactory factory = new AccountFactory(config);
        assertEquals("http://find.me", factory.createAccount().getPublicURL());
    }

    @Test
    public void constructImpl() {
        AccountConfig config = new AccountConfig();
        AccountFactory factory = new AccountFactory(config);
        factory.setHttpClient(httpClient);
        factory.createAccount();
    }

}
