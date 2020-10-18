package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.SecretSantaBroadcastOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecretSantaServiceLambdaHandlerTest {

    @Test
    void callsAppLayer() {
        // -- setup --
        SecretSantaService mockAppHandler = mockedAppHandler(new SecretSantaBroadcastInput(), new SecretSantaBroadcastOutput());
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockAppHandler);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertNotNull(lambdaReply);
        verify(mockAppHandler).broadcastMessage(any());
    }

    private SecretSantaService mockedAppHandler(final SecretSantaBroadcastInput expectedInput, final SecretSantaBroadcastOutput stubbedOutput) {
        SecretSantaService mockAppHandler = mock(SecretSantaService.class);
        when(mockAppHandler.broadcastMessage(expectedInput)).thenReturn(stubbedOutput);

        return mockAppHandler;
    }
}