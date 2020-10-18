package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSantaMessenger;
import com.frj.secretsanta.app.SecretSantaInput;
import com.frj.secretsanta.app.SecretSantaOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecretSantaLambdaHandlerTest {

    @Test
    void callsAppLayer() {
        // -- setup --
        SecretSantaMessenger mockAppHandler = mockedAppHandler(new SecretSantaInput(), new SecretSantaOutput());
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockAppHandler);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertNotNull(lambdaReply);
        verify(mockAppHandler).handle(any());
    }

    private SecretSantaMessenger mockedAppHandler(final SecretSantaInput expectedInput, final SecretSantaOutput stubbedOutput) {
        SecretSantaMessenger mockAppHandler = mock(SecretSantaMessenger.class);
        when(mockAppHandler.handle(expectedInput)).thenReturn(stubbedOutput);

        return mockAppHandler;
    }
}