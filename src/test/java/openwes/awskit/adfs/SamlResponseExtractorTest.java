package openwes.awskit.adfs;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class SamlResponseExtractorTest {
    @Test
    public void shouldExtractSamlResponse() throws IOException {
        String template = Resources.toString(Resources.getResource("adfs/response_success.hbs"), Charsets.UTF_8);
        final String assertion = "assertion_123";
        Handlebars handlebars = new Handlebars();
        String html = handlebars.compileInline(template).apply(assertion);
        SamlResponse response = new SamlResponseExtractor().extract(html);
        assertThat(response.isSuccess(), is(true));
        assertThat(response.getAssertion(), is(assertion));
        assertThat(response.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void shouldDetectError() throws IOException {
        String template = Resources.toString(Resources.getResource("adfs/response_failure.hbs"), Charsets.UTF_8);
        final String errorMessage = "Incorrect user ID or password. Type the correct user ID and password, and try again.";
        Handlebars handlebars = new Handlebars();
        String html = handlebars.compileInline(template).apply(errorMessage);
        SamlResponse response = new SamlResponseExtractor().extract(html);
        assertThat(response.isSuccess(), is(false));
        assertThat(response.getAssertion(), is(nullValue()));
        assertThat(response.getErrorMessage(), is(errorMessage));
    }
}