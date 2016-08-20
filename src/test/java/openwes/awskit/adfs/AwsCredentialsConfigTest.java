package openwes.awskit.adfs;

import com.amazonaws.services.securitytoken.model.Credentials;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class AwsCredentialsConfigTest {
    @Test
    public void shouldPrintOriginal() throws IOException {
        String original = Resources.toString(Resources.getResource("adfs/sample_credentials"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(original);
        assertThat(config.print().trim(), is(original));
    }

    @Test
    public void shouldCreateNew() throws IOException {
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        Credentials credentials = new Credentials("sample_key_id", "sample_access_key", "sample_session_token", new Date());
        config.setCredentials("default", credentials);
        assertThat(config.print().trim(), is(expected));
    }

    @Test
    public void shouldCreateNewFromEmptyFile() throws IOException {
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(StringUtils.EMPTY);
        Credentials credentials = new Credentials("sample_key_id", "sample_access_key", "sample_session_token", new Date());
        config.setCredentials("default", credentials);
        assertThat(config.print().trim(), is(expected));
    }

    @Test
    public void shouldModify() throws IOException {
        String original = Resources.toString(Resources.getResource("adfs/sample_credentials"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials2"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(original);
        Credentials credentials = new Credentials("sample_key_id2", "sample_access_key2", "sample_session_token2", new Date());
        config.setCredentials("default", credentials);
        assertThat(config.print().trim(), is(expected));
    }

    @Test
    public void shouldAppend() throws IOException {
        String original = Resources.toString(Resources.getResource("adfs/sample_credentials"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials3"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(original);
        Credentials credentials = new Credentials("sample_key_id2", "sample_access_key2", "sample_session_token2", new Date());
        config.setCredentials("another", credentials);
        assertThat(config.print().trim(), is(expected));
    }

    @Test
    public void shouldModifyInMultipleProfiles() throws IOException {
        String original = Resources.toString(Resources.getResource("adfs/sample_credentials3"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials4"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(original);
        Credentials credentials = new Credentials("sample_key_id1", "sample_access_key1", "sample_session_token1", new Date());
        config.setCredentials("default", credentials);
        assertThat(config.print().trim(), is(expected));
    }

    @Test
    public void shouldModifyInMultipleProfiles2() throws IOException {
        String original = Resources.toString(Resources.getResource("adfs/sample_credentials4"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("adfs/sample_credentials5"), Charsets.UTF_8);
        AwsCredentialsConfig config = new AwsCredentialsConfig();
        config.load(original);
        Credentials credentials = new Credentials("sample_key_id3", "sample_access_key3", "sample_session_token3", new Date());
        config.setCredentials("another", credentials);
        assertThat(config.print().trim(), is(expected));
    }
}