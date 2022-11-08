package it.sevenbits.quizzes.core.models;

/**
 * Model for rules
 */
public class Rules {
    private final String rules;

    /**
     * Rules constructor
     * @param rules - rules themselves
     */
    public Rules(final String rules) {
        this.rules = rules;
    }

    /**
     * Getter for rules
     * @return rules
     */
    public String getRules() {
        return rules;
    }
}
