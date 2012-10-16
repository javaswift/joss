package nl.tweeenveertig.openstack.client.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
public class AccountFactoryImplTest {

    @Test(expected = NullPointerException.class)
    public void construct() throws Exception {
        AccountFactoryImpl factory = new AccountFactoryImpl();
        factory.setAuthUrl(null);
        factory.setPassword(null);
        factory.setTenant(null);
        factory.setUsername(null);

        final ClientImpl mockedClient = mock(ClientImpl.class);
        whenNew(ClientImpl.class).withNoArguments().thenReturn(mockedClient);
        when(mockedClient.authenticate(anyString(), anyString(), anyString(), anyString())).thenReturn(new AccountImpl(null, null, null));
        assertNotNull(factory.getAccount());
    }
}
