package openwes.awskit.adfs;

import com.amazonaws.services.securitytoken.model.Credentials;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class AwsCredentialsConfig {
    private INIConfiguration config;

    public AwsCredentialsConfig() {
        this.config = new INIConfiguration();
    }

    public void load(String credentials) {
        try(
            Reader reader = new StringReader(credentials)
        ) {
            config.read(reader);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFile(String path) {
        try(
            Reader reader = new FileReader(path)
        ) {
            config.read(reader);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDefaultFile() {
        loadFile(getDefaultFilePath());
    }

    public void saveFile(String path) {
        try (
            Writer writer = new FileWriter(path)
        ) {
            this.config.write(writer);
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDefaultFile() {
        saveFile(getDefaultFilePath());
    }

    public String print() {
        try (
            Writer writer = new StringWriter();
        ) {
            this.config.write(writer);
            return writer.toString();
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCredentials(String profile, Credentials credentials) {
        config.setProperty(String.format("%s.%s", profile, "aws_access_key_id"), credentials.getAccessKeyId());
        config.setProperty(String.format("%s.%s", profile, "aws_secret_access_key"), credentials.getSecretAccessKey());
        config.setProperty(String.format("%s.%s", profile, "aws_session_token"), credentials.getSessionToken());
        config.setProperty(String.format("%s.%s", profile, "aws_security_token"), credentials.getSessionToken());
    }

    private static String getDefaultFilePath() {
        return String.format("%s/.aws/credentials", System.getProperty("user.home"));
    }
}
