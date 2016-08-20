package openwes.awskit.adfs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SamlResponseExtractor {
    public SamlResponse extract(String html) {
        SamlResponse response = new SamlResponse();
        Document doc = Jsoup.parse(html);
        Element errorTextElement = doc.select("form#loginForm #errorText").first();
        if (errorTextElement != null) {
            response.setSuccess(false);
            response.setErrorMessage(errorTextElement.text());
        } else {
            Element responseElement = doc.select("form[name=hiddenform] input[name=SAMLResponse]").first();
            if (responseElement != null) {
                response.setSuccess(true);
                response.setAssertion(responseElement.attr("value"));
            } else {
                response.setSuccess(false);
                response.setErrorMessage("Empty SAMLResponse");
            }
        }
        return response;
    }
}
