package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSanta;
import com.frj.secretsanta.app.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.SecretSantaBroadcastOutput;
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
        SecretSanta mockAppHandler = mockedAppHandler(new SecretSantaBroadcastInput(), new SecretSantaBroadcastOutput());
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockAppHandler);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertNotNull(lambdaReply);
        verify(mockAppHandler).broadcastMessage(any());
    }

    private SecretSanta mockedAppHandler(final SecretSantaBroadcastInput expectedInput, final SecretSantaBroadcastOutput stubbedOutput) {
        SecretSanta mockAppHandler = mock(SecretSanta.class);
        when(mockAppHandler.broadcastMessage(expectedInput)).thenReturn(stubbedOutput);

        return mockAppHandler;
    }
}