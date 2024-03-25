package life.macchiato.courses.util;

import org.htmlunit.WebClient;

public class WebClientBuilder {
    private boolean cssEnabled = false;
    private boolean jsEnabled = false;
    private boolean appletEnabled = false;

    public WebClient build() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(cssEnabled);
        client.getOptions().setJavaScriptEnabled(jsEnabled);
        client.getOptions().setAppletEnabled(appletEnabled);
        return client;
    }

    public WebClientBuilder enableCss() {
        cssEnabled = true;
        return this;
    }
    public WebClientBuilder enableJs() {
        jsEnabled = true;
        return this;
    }
    public WebClientBuilder enableApplet() {
        jsEnabled = true;
        return this;
    }
}
