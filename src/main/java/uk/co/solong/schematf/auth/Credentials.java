package uk.co.solong.schematf.auth;

public class Credentials {

    private final String remoteUrl;
    private final String token;

    public Credentials(String remoteUrl, String token) {
        this.remoteUrl = remoteUrl;
        this.token = token;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getToken() {
        return token;
    }

}
