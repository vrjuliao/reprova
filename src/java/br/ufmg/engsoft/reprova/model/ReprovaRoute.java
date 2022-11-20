package br.ufmg.engsoft.reprova.model;

public abstract class ReprovaRoute {

    /**
     * Access token.
     */
    protected static final String TOKEN = Environments.getInstance().getToken();

    /**
     * Messages.
     */
    protected static final String UNAUTHORIZED = "\"Unauthorized\"";
    protected static final String INVALID = "\"Invalid request\"";
    protected static final String OK = "\"Ok\"";

    /**
     * Check if the given token is authorized.
     */
    protected static boolean authorized(String token) {
        return ReprovaRoute.TOKEN.equals(token);
    }

}
