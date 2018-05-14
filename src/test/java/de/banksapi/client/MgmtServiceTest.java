package de.banksapi.client;

import de.banksapi.client.model.incoming.mgmt.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.mgmt.UserOut;
import de.banksapi.client.services.MgmtService;
import de.banksapi.client.services.OAuth2Service;
import de.banksapi.client.services.internal.HttpClient.Response;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static de.banksapi.client.TestAuthData.ADMIN_CLIENT_PASSWORD;
import static de.banksapi.client.TestAuthData.ADMIN_CLIENT_USERNAME;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MgmtServiceTest implements BanksapiTest {

    private static MgmtService mgmtService;
    private static Tenant tenant;
    private static Client client;
    private static ClientRole clientRole;
    private static UUID addedUser;

    @BeforeClass
    public static void setUp() throws Exception {
        OAuth2Token token = new OAuth2Service().getClientToken(ADMIN_CLIENT_USERNAME, ADMIN_CLIENT_PASSWORD);
        mgmtService = new MgmtService(token);
        Response<TenantList> tenantResponse = mgmtService.getTenants();
        tenant = tenantResponse.getData().get(0);
        Response<ClientList> clientResponse = mgmtService.getClients(tenant.getName());
        client = clientResponse.getData().get(0);

        if (client.getClientRoles().iterator().hasNext()) {
            clientRole = client.getClientRoles().iterator().next();
        }
    }

    @Test
    public void test01GetTenants() {
        Response<TenantList> response = mgmtService.getTenants();
        basicResponseCheckData(response, 200, "get tenants");
        assert response.getData().size() > 0 : "Number of Tenants not greater 0";

        tenant = response.getData().get(0);
    }

    @Test
    public void test02GetTenant() {
        Response<Tenant> response = mgmtService.getTenant(tenant.getName());
        basicResponseCheckData(response, 200, "get tenant");

        Tenant myTenant = response.getData();
        assert myTenant.getName().equals(tenant.getName())
                : "name from getTenants() doesn't equal name from getTenant()";
        assert myTenant.getDescription().equals(tenant.getDescription())
                : "description from getTenants() doesn't equal description from getTenant()";
    }

    @Test
    public void test03GetClients() {
        Response<ClientList> response = mgmtService.getClients(tenant.getName());
        basicResponseCheckData(response, 200, "get clients");
        assert response.getData().size() > 0 : "Number of clients not greater than 0";
    }

    @Test
    public void test04GetClient() {
        Response<Client> response = mgmtService.getClient(tenant.getName(), client.getName());
        basicResponseCheckData(response, 200, "get client");

        Client myClient = response.getData();

        assert myClient.getName().equals(client.getName())
                : "name from getClients() doesn't equal name from getClient()";
        assert myClient.getDescription().equals(client.getDescription())
                : "description from getClients() doesn't equal description from getClient()";
        assert myClient.getUsername().equals(client.getUsername())
                : "username from getClients() doesn't equal username from getClient()";
        myClient.getGrantedScopes().forEach(scope -> {
            assert client.getGrantedScopes().contains(scope) : "Scope " + scope + " not granted " +
                    "when using getClients() but when using getClient()";
        });
        client.getGrantedScopes().forEach(scope -> {
            assert myClient.getGrantedScopes().contains(scope) : "Scope " + scope + " not " +
                    "granted when using getClient() but when using getClients()";
        });
        myClient.getClientRoles().forEach(role -> {
            assert client.getClientRoles().contains(role) : "Role " + role.getName() + " not " +
                    "assigned when using getClients() but when using getClient()";
        });
        client.getClientRoles().forEach(role -> {
            assert myClient.getClientRoles().contains(role) : "Role " + role.getName() + " not " +
                    "assigned when using getClient() but when using getClients()";
        });
    }

    @Test
    public void test05GetClientRoles() {
        Response<ClientRoleList> response = mgmtService.getClientRoles(tenant.getName(),
                client.getName());
        basicResponseCheckData(response, 200, "get client roles");

        ClientRoleList clientRoles = response.getData();

        clientRoles.forEach(role -> {
            assert client.getClientRoles().contains(role) : "Role " + role.getName() + " not " +
                    "assigned when using getClients() but when using getClientRoles()";
        });
        client.getClientRoles().forEach(role -> {
            assert clientRoles.contains(role) : "Role " + role.getName() + " not assigned when " +
                    "using getClientRoles() but when using getClients()";
        });
    }

    @Test
    public void test06GetClientRole() {
        // check that a random client role can not be retrieved
        Response<ClientRole> responseX = mgmtService.getClientRole(tenant.getName(),
                client.getName(), generateRandomString());
        basicResponseCheck(responseX, 404);

        // if the client has at least one client role, check that that role can be retrieved
        if (clientRole != null) {
            Response<ClientRole> response = mgmtService.getClientRole(tenant.getName(),
                    client.getName(), clientRole.getName());
            basicResponseCheckData(response, 200, "get client role");

            ClientRole role = response.getData();

            assert role.getName().equals(clientRole.getName()) : "Role name from " +
                    "getClientRole() does not match the one from getClients()";
        }
    }

    @Test
    public void test07AddUser() {
        UserOut user = new UserOut(generateRandomString(), generateRandomString(),
                generateRandomString(), generateRandomString());

        Response<String> response = mgmtService.addUser(tenant.getName(), user);
        basicResponseCheck(response, 201);
    }

    @Test
    public void test08GetUsers() {
        Response<UserInList> response = mgmtService.getUsers(tenant.getName());
        basicResponseCheckData(response, 200, "get users");

        assert response.getData().size() > 0 : "tenant without users";

        addedUser = response.getData().get(0).getId();
    }

    @Test
    public void test09GetUser() {
        Response<UserIn> response404 = mgmtService.getUser(tenant.getName(), UUID.randomUUID());
        basicResponseCheck(response404, 404);

        Response<UserIn> response200 = mgmtService.getUser(tenant.getName(), addedUser);
        basicResponseCheckData(response200, 200, "get user");
    }

    @Test
    public void test10GetClientRoleUsers() {
        Response<UUIDList> response404 = mgmtService.getClientRoleUsers(tenant.getName(),
                client.getName(), generateRandomString());
        basicResponseCheck(response404, 404);

        if (clientRole != null) {
            Response<UUIDList> response200 = mgmtService.getClientRoleUsers(tenant.getName(),
                    client.getName(), clientRole.getName());
            basicResponseCheckData(response200, 200, "get client role users");
        }
    }

}
