package io.jans.idp.keycloak.service;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import io.jans.as.common.util.AttributeConstants;
import io.jans.as.common.model.common.User;
//import io.jans.configapi.plugin.mgt.model.user.CustomUser;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UsersApiLegacyService {

    private static Logger LOG = LoggerFactory.getLogger(UsersApiLegacyService.class);
    private static String AUTH_USER_ENDPOINT = "http://pujavs-settling-giraffe.gluu.info/jans-config-api/mgt/configuser";
    public static final String  AUTH_TOKEN = "0aaa783f-c357-4d51-9e4f-93df99b561db";

    private KeycloakSession session;
    private ComponentModel model;
    
    public UsersApiLegacyService(KeycloakSession session,ComponentModel model) {
        LOG.error(" session:{}, model:{}", session, model);
        System.out.println("UsersApiLegacyService() - session = "+session+" , model = "+model);
        this.session = session;
        this.model = model;
    }
    
    public User getUserById(String inum) {
        LOG.error(" inum:{}", inum);
        System.out.println("inum = "+inum);
        try {
            System.out.println("UsersApiLegacyService()::getUserById() - inum = "+inum);
            return SimpleHttp.doGet(AUTH_USER_ENDPOINT +"/"+ inum, this.session).asJson(User.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Error fetching user based on inum:{} from external service is:{} - {} ", inum, ex.getMessage(), ex);
            System.out.println("Error fetching user based on inum="+inum+" from external service is->"+ex);
        }
        return null;
    }
        
    public User getUserByName(String username) {
        LOG.error(" username:{}", username);
        try {
            System.out.println("UsersApiLegacyService()::getUserByName() - username = "+username);
            //return SimpleHttp.doGet(AUTH_USER_ENDPOINT + username, this.session).asJson(User.class);
            return makeGetRequest(AUTH_USER_ENDPOINT +"?pattern="+username ,AUTH_TOKEN);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Error fetching user based on username:{} from external service is:{} - {} ", username, ex.getMessage(), ex);
            System.out.println("Error fetching user based on username="+username+" from external service is->"+ex);
        }
        return null;
    }
    
    public User getUserByEmail(String email) {
        LOG.error(" email:{}", email);
        try {
            System.out.println("UsersApiLegacyService()::getUserById() - email = "+email);
            return makeGetRequest(AUTH_USER_ENDPOINT +"?pattern="+email ,AUTH_TOKEN);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error("Error fetching user based on email:{} from external service is:{} - {} ", email, ex.getMessage(), ex);
            System.out.println("Error fetching user based on email="+email+" from external service is->"+ex);
        }
        return null;
    }
    
    private User makeGetRequestNew(String uri, String accessToken) throws IOException {
        LOG.error(" makeGetRequest() - uri:{}, accessToken:{}", uri, accessToken);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - uri = "+uri+" , accessToken ="+accessToken);
       
        Builder clientRequest = getClientBuilder(uri);    
        clientRequest.header("Authorization", "Bearer " + accessToken);
        LOG.error(" makeGetRequest() - clientRequest:{}", clientRequest);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - clientRequest = "+clientRequest+" \n\n");

        Response response = clientRequest.get();
        LOG.error(" makeGetRequest() - response:{}", response);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - response = "+response+" \n\n");
        
       //User user = SimpleHttp.doGet(AUTH_USER_ENDPOINT, clientRequest).asJson(User.class);
       //LOG.error(" makeGetRequest() - user:{}", user);
       //System.out.println("UsersApiLegacyService()::makeGetRequest() - user = "+user+" \n\n");
        
       
        
        return null;
    }
    
    
    private User makeGetRequest(String uri, String accessToken) throws IOException {
        LOG.error(" makeGetRequest() - uri:{}, accessToken:{}", uri, accessToken);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - uri = "+uri+" , accessToken ="+accessToken);
       
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);        
        request.addHeader("Authorization", "Bearer " + accessToken);
        request.setHeader("Authorization", "Bearer " + accessToken);
        LOG.error(" makeGetRequest() - client:{}, request:{}, request.getAllHeaders():{}", client, request,request.getAllHeaders());
        System.out.println("UsersApiLegacyService()::makeGetRequest() - client = "+client+" ,request = "+request+" \n\n");

        HttpResponse response =  client.execute(request);
        LOG.error(" makeGetRequest() - response:{}", response);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - response = "+response+" \n\n");
        
       
        User user = SimpleHttp.doGet(AUTH_USER_ENDPOINT, client).header("Authorization", "Bearer " + accessToken).asJson(User.class);
        LOG.error(" makeGetRequest() - user:{}", user);
        System.out.println("UsersApiLegacyService()::makeGetRequest() - user = "+user+" \n\n");
        
        return user;
    }
    
    private static Builder getClientBuilder(String url) {
        return ClientBuilder.newClient().target(url).request();
    }
    
    /*private HttpResponse makeRequest(String uri, String userName, String password, boolean sendRpt, String resourceId) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        String accessToken = TestsHelper.getToken(userName, password, TestsHelper.testRealm);
        String rpt;

        if (sendRpt) {
            rpt = obtainRequestingPartyToken(resourceId, accessToken);
        } else {
            rpt = accessToken;
        }

        request.addHeader("Authorization", "Bearer " + rpt);

        return client.execute(request);
    }*/
    
    

}
