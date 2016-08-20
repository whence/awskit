package openwes.awskit.adfs;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.AmazonIdentityManagementException;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithSAMLRequest;
import com.amazonaws.services.securitytoken.model.Credentials;
import org.apache.commons.lang3.StringUtils;

public class Login {
    public static Credentials login(String roleArn, String principalArn, String assertion) {
        BasicAWSCredentials blankCredentials =new BasicAWSCredentials(StringUtils.EMPTY, StringUtils.EMPTY);
        AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient(blankCredentials);
        AssumeRoleWithSAMLRequest req = new AssumeRoleWithSAMLRequest();
        req.setRoleArn(roleArn);
        req.setPrincipalArn(principalArn);
        req.setSAMLAssertion(assertion);
        req.setDurationSeconds(3600);
        return client.assumeRoleWithSAML(req).getCredentials();
    }

    public static boolean isExpired(String profile) {
        ProfileCredentialsProvider provider = new ProfileCredentialsProvider(profile);
        AmazonIdentityManagementClient client = new AmazonIdentityManagementClient(provider);
        try {
            client.getAccountSummary();
        } catch (AmazonIdentityManagementException | NullPointerException e) {
            return true;
        }
        return false;
    }
}
