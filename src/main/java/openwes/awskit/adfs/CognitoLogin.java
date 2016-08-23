package openwes.awskit.adfs;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityResult;
import com.amazonaws.services.cognitoidentity.model.GetIdRequest;
import com.amazonaws.services.cognitoidentity.model.GetIdResult;
import com.amazonaws.services.securitytoken.model.Credentials;

import java.util.HashMap;
import java.util.Map;

public class CognitoLogin implements Login {
    /**
     * https://mobile.awsblog.com/post/Tx28TCWLHIRK4GT/Announcing-SAML-Support-for-Amazon-Cognito
     */
    @Override
    public Credentials login(String roleArn, String principalArn, String assertion) {
        AmazonCognitoIdentity client = new AmazonCognitoIdentityClient(new AnonymousAWSCredentials());
        GetIdRequest idRequest = new GetIdRequest();
        idRequest.setAccountId("account id");
        idRequest.setIdentityPoolId("identity_pool_id");

        Map<String, String> logins = new HashMap<>();
        logins.put(principalArn, assertion);
        idRequest.setLogins(logins);
        GetIdResult idResp = client.getId(idRequest);
        String identityId = idResp.getIdentityId();

        GetCredentialsForIdentityRequest credRequest = new GetCredentialsForIdentityRequest();
        credRequest.setIdentityId(identityId);
        credRequest.setLogins(logins);

        credRequest.setCustomRoleArn(roleArn);

        GetCredentialsForIdentityResult credetialsResult = client.getCredentialsForIdentity(credRequest);
        return null;
    }
}
