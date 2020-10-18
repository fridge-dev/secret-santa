package com.frj.secretsanta.lambda;

import java.util.Set;

public class SecretSantaLambdaReply {

    private String status;
    private Set<String> failedIndividuals;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFailedIndividuals(Set<String> failedIndividuals) {
        this.failedIndividuals = failedIndividuals;
    }

    public Set<String> getFailedIndividuals() {
        return failedIndividuals;
    }
}
