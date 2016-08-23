package openwes.awskit.adfs;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.AmazonIdentityManagementException;

public class AwsProfileUtils {
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
