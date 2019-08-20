package de.banksapi.client.services;

import de.banksapi.client.model.incoming.mgmt.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.mgmt.UserOut;
import de.banksapi.client.services.internal.HttpClient.Response;
import de.banksapi.client.services.internal.HttpHelper;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

import static de.banksapi.client.BANKSapi.getBanksapiBase;
import static de.banksapi.client.services.internal.HttpHelper.buildUrl;

/**
 * This service interfaces with the BANKSapi Auth Management API. This API provides management
 * methods for the authentication API to control clients, users and roles.
 *
 * @see <a href="https://docs.banksapi.de/#banksapi-auth-api6">
 * BANKSapi Auth Management API</a>
 */
public class MgmtService implements OAuthAwareService {

    private final static URL AUTH_CONTEXT = HttpHelper.buildUrl(getBanksapiBase(), "auth/");

    private static final String PATH_FMT_TENANTS = "mgmt/v1/tenants";
    private static final String PATH_FMT_TENANT = "mgmt/v1/tenants/%s";
    private static final String PATH_FMT_CLIENTS = "mgmt/v1/tenants/%s/clients";
    private static final String PATH_FMT_CLIENT = "mgmt/v1/tenants/%s/clients/%s";
    private static final String PATH_FMT_USERS = "mgmt/v1/tenants/%s/users";
    private static final String PATH_FMT_USER = "mgmt/v1/tenants/%s/users/%s";
    private static final String PATH_FMT_USER_DEACTIVATE = "mgmt/v1/tenants/%s/users/%s/deactivate";
    private static final String PATH_FMT_USER_REACTIVATE = "mgmt/v1/tenants/%s/users/%s/reactivate";
    private static final String PATH_FMT_ROLES = "mgmt/v1/tenants/%s/clients/%s/roles";
    private static final String PATH_FMT_ROLE = "mgmt/v1/tenants/%s/clients/%s/roles/%s";
    private static final String PATH_FMT_ROLE_USERS = "mgmt/v1/tenants/%s/clients/%s/roles/%s/users";
    private static final String PATH_FMT_ROLE_USER = "mgmt/v1/tenants/%s/clients/%s/roles/%s/users/%s";

    private OAuth2Token oAuth2Token;

    /**
     * Creates a new instance of the management service.
     *
     * @param oAuth2Token a valid OAuth2 token to send along all requests
     */
    public MgmtService(OAuth2Token oAuth2Token) {
        Objects.requireNonNull(oAuth2Token);
        this.oAuth2Token = oAuth2Token;
    }

    public Response<TenantList> getTenants() {
        URL tenantsUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_TENANTS);
        return createAuthenticatingHttpClient(tenantsUrl).get(TenantList.class);
    }

    public Response<Tenant> getTenant(String tenantName) {
        URL tenantUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_TENANT, tenantName);
        return createAuthenticatingHttpClient(tenantUrl).get(Tenant.class);
    }

    public Response<ClientList> getClients(String tenantName) {
        URL clientsUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_CLIENTS, tenantName);
        return createAuthenticatingHttpClient(clientsUrl).get(ClientList.class);
    }

    public Response<Client> getClient(String tenantName, String clientName) {
        URL clientUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_CLIENT, tenantName, clientName);
        return createAuthenticatingHttpClient(clientUrl).get(Client.class);
    }

    public Response<ClientRoleList> getClientRoles(String tenantName, String clientName) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_ROLES, tenantName, clientName);
        return createAuthenticatingHttpClient(rolesUrl).get(ClientRoleList.class);
    }

    public Response<ClientRole> getClientRole(String tenantName, String clientName, String roleName) {
        URL roleUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_ROLE, tenantName, clientName, roleName);
        return createAuthenticatingHttpClient(roleUrl).get(ClientRole.class);
    }

    public Response<UUIDList> getClientRoleUsers(String tenantName, String clientName, String roleName) {
        URL roleUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_ROLE_USERS, tenantName, clientName, roleName);
        return createAuthenticatingHttpClient(roleUrl).get(UUIDList.class);
    }

    public Response<String> addClientRoleUser(String tenantName, String clientName, String roleName,
            UUID userId) {
        URL roleUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_ROLE_USERS, tenantName, clientName, roleName);
        return createAuthenticatingHttpClient(roleUrl).post(userId.toString(), String.class);
    }

    public Response<UUID> getClientRoleUser(String tenantName, String clientName, String roleName,
            UUID userId) {
        URL roleUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_ROLE_USER, tenantName, clientName, roleName,
                userId.toString());
        return createAuthenticatingHttpClient(roleUrl).get(UUID.class);
    }

    public Response<UserInList> getUsers(String tenantName) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_USERS, tenantName);
        return createAuthenticatingHttpClient(rolesUrl).get(UserInList.class);
    }

    public Response<UserIn> getUser(String tenantName, UUID userId) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_USER, tenantName, userId.toString());
        return createAuthenticatingHttpClient(rolesUrl).get(UserIn.class);
    }

    public Response<String> addUser(String tenantName, UserOut user) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_USERS, tenantName);
        return createAuthenticatingHttpClient(rolesUrl).post(user, String.class);
    }

    public Response deactivateUser(String tenantName, UUID userId) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_USER_DEACTIVATE, tenantName, userId.toString());
        return createAuthenticatingHttpClient(rolesUrl).put(null);
    }

    public Response<UserIn> reactivateUser(String tenantName, UUID userId, UserOut user) {
        URL rolesUrl = buildUrl(AUTH_CONTEXT, PATH_FMT_USER_REACTIVATE, tenantName, userId.toString());
        return createAuthenticatingHttpClient(rolesUrl).post(user, UserIn.class);
    }

    @Override
    public OAuth2Token getOAuth2Token() {
        return oAuth2Token;
    }

}
