package it.sevenbits.quizzes.web.models;

/**
 * Web Socket message model that contains its name and useful information (payload). This model is needed for exchanging
 * messages between client and server through web sockets.
 */
public class WSMessage {
    private final String name;
    private final String payload;

    /**
     * Web Socket message constructor
     * @param name - name of the message
     * @param payload - payload the message
     */
    public WSMessage(final String name, final String payload) {
        this.name = name;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public String getPayload() {
        return payload;
    }
}
