package nl.tweeenveertig.openstack.client.factory;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.client.impl.ClientImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AccountFactory.class)
public class AccountFactoryTest {

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
        AccountFactory factory = new AccountFactory();
        AccountConfig config = new AccountConfig();
        config.setAuthUrl(null);
        config.setPassword(null);
        config.setTenant(null);
        config.setUsername(null);
        factory.setConfig(config);
        final ClientImpl mockedClient = mock(ClientImpl.class);
        whenNew(ClientImpl.class).withNoArguments().thenReturn(mockedClient);
        when(mockedClient.authenticate(anyString(), anyString(), anyString(), anyString())).thenReturn(new AccountImpl(null, null, null));
        assertNotNull(factory.createAccount());
    }

}
