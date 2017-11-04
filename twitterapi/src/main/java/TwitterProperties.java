import java.io.InputStream;
import java.util.Properties;

class TwitterProperties {

    private String apikey;

    private String apisecret;

    private String token;

    private String tokensecret;

    public TwitterProperties(String apikey, String apisecret, String token, String tokensecret) {
        this.apikey = apikey;
        this.apisecret = apisecret;
        this.token = token;
        this.tokensecret = tokensecret;
    }

    String getApikey() {
        return apikey;
    }

    String getApisecret() {
        return apisecret;
    }

    String getToken() {
        return token;
    }

    String getTokensecret() {
        return tokensecret;
    }
}
