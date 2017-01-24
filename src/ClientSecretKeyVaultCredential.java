import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.windowsazure.core.pipeline.filter.ServiceRequestContext;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.*;


public class ClientSecretKeyVaultCredential extends KeyVaultCredentials {

    private String applicationId ;
    private String applicationSecret;


    public ClientSecretKeyVaultCredential(String applicationId, String applicationSecret)
    {
        this.setApplicationId(applicationId);
        this.setApplicationSecret(applicationSecret);
    }


    @Override
    public Header doAuthenticate(ServiceRequestContext serviceRequestContext, Map<String, String> map) {
        AuthenticationResult result = null;
        String authorization = map.get("authorization");
        String resource      = map.get("resource");
        try {
            result = getAccessToken(authorization, resource);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new BasicHeader("Authorization", result.getAccessTokenType() + " " + result.getAccessToken());
    }

    private AuthenticationResult getAccessToken(String authorization, String resource)
        throws InterruptedException, ExecutionException {
        AuthenticationContext context = null;
        ExecutorService service = Executors.newFixedThreadPool(1);

        try {
            context = new AuthenticationContext(authorization, false, service);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Future<AuthenticationResult> resultFuture = context.acquireToken(
                resource,
                new ClientCredential(getApplicationId(),
                        getApplicationSecret()),
                null);

        AuthenticationResult res = resultFuture.get();
        service.shutdown();
        return res;

    }




    /*
    // Setters and Getters
     */
    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
