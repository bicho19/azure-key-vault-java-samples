
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.KeyVaultClientService;
import com.microsoft.azure.keyvault.KeyVaultConfiguration;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.Secret;

import com.microsoft.windowsazure.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 *
 * Azure Key Vault sample for getting secret value from vault
 *  
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException, URISyntaxException, UnsupportedEncodingException {
        //Azure Active Directory Application ID (Client ID)
        String ADclientId = "0accbbc0-****-****-****-************";

        //Azure Active Directory Application Secret
        String ADappKey   = "7mTRYvuls***********************************";



        KeyVaultCredentials kvCred = new ClientSecretKeyVaultCredential(ADclientId,ADappKey);
        Configuration config = KeyVaultConfiguration.configure(null,kvCred);
        KeyVaultClient kvClient = KeyVaultClientService.create(config);

        /**
         * Replace it with your KeyVault link
         */
        String secretIdentifier = "https://{vault-name}.vault.azure.net/secrets/{secret-name}/";

        Future<Secret> secretFuture = kvClient.getSecretAsync(secretIdentifier);
        String secretValue = secretFuture.get().getValue();

        System.out.println("output ... ");
        System.out.println(secretValue);





    }



}
