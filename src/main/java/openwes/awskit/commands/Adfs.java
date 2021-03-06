package openwes.awskit.commands;

import com.amazonaws.services.securitytoken.model.Credentials;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import openwes.awskit.adfs.AssumeRoleLogin;
import openwes.awskit.adfs.AwsCredentialsConfig;
import openwes.awskit.adfs.AwsProfileUtils;
import openwes.awskit.adfs.Login;
import openwes.awskit.adfs.SamlFetcher;
import openwes.awskit.adfs.SamlResponse;
import openwes.awskit.adfs.SamlResponseExtractor;

import java.io.IOException;

@Parameters(separators = "=", commandDescription = "Refresh ADFS token for AWS")
public class Adfs implements Runnable {
    @Parameter(names = "--host", description = "The ADFS host", required = true)
    private String host;

    @Parameter(names = "--username", description = "Username to login to The ADFS", required = true)
    private String username;

    @Parameter(names = "--password", description = "Password to login to the ADFS", required = true)
    private String password;

    @Parameter(names = "--role-arn", description = "Role ARN to login to AWS", required = true)
    private String roleArn;

    @Parameter(names = "--principal-arn", description = "Principal ARN to login to AWS", required = true)
    private String principalArn;

    @Parameter(names = "--profile", description = "AWS Profile to put into credentials file")
    private String profile = "default";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getPrincipalArn() {
        return principalArn;
    }

    public void setPrincipalArn(String principalArn) {
        this.principalArn = principalArn;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void run() {
        if (AwsProfileUtils.isExpired(profile)) {
            System.out.print("Fetching SAML...");
            String html;
            try {
                html = new SamlFetcher().fetchRoles(host, username, password);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Done");

            System.out.print("Extracting SAML response...");
            SamlResponse response = new SamlResponseExtractor().extract(html);

            if (response.isSuccess()) {
                System.out.println("Done");

                System.out.print("Obtaining Credentials...");
                Login login = new AssumeRoleLogin();
                Credentials credentials = login.login(roleArn, principalArn, response.getAssertion());
                System.out.println("Done");

                System.out.print("Updating credentials file...");
                AwsCredentialsConfig config = new AwsCredentialsConfig();
                config.loadDefaultFile();
                config.setCredentials(profile, credentials);
                config.saveDefaultFile();
                System.out.println("Done");
            } else {
                System.out.println("Failed.");
                System.out.println("Reason: " + response.getErrorMessage());
            }
        } else {
            System.out.println("The current login is still valid. No action required.");
        }

    }
}
