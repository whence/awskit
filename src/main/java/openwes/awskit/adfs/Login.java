package openwes.awskit.adfs;

import com.amazonaws.services.securitytoken.model.Credentials;

public interface Login {
    Credentials login(String roleArn, String principalArn, String assertion);
}
