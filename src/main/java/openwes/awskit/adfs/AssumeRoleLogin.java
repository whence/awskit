package openwes.awskit.adfs;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithSAMLRequest;
import com.amazonaws.services.securitytoken.model.Credentials;

public class AssumeRoleLogin implements Login {
    @Override
    public Credentials login(String roleArn, String principalArn, String assertion) {
        AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient(new AnonymousAWSCredentials());
        AssumeRoleWithSAMLRequest req = new AssumeRoleWithSAMLRequest();
        req.setRoleArn(roleArn);
        req.setPrincipalArn(principalArn);
        req.setSAMLAssertion(assertion);
        req.setDurationSeconds(3600);
        return client.assumeRoleWithSAML(req).getCredentials();
    }
}
