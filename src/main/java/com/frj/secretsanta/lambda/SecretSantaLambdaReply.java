package com.frj.secretsanta.lambda;

import java.util.Set;

public class SecretSantaLambdaReply {

    private String status;
    private Set<String> failedIndividuals;
    private String errorMessage;

    public static SecretSantaLambdaReply success() {
        SecretSantaLambdaReply reply = new SecretSantaLambdaReply();
        reply.setStatus("200");

        return reply;
    }

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

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
