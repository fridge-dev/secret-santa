package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
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
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();
        ImmutableSecretSantaBroadcastInput appInput = ImmutableSecretSantaBroadcastInput.builder()
                .build();
        ImmutableSecretSantaBroadcastOutput appOutput = ImmutableSecretSantaBroadcastOutput.builder()
                .build();
        SecretSantaService mockApp = mockedAppHandler(appInput, appOutput);

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockApp);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertNotNull(lambdaReply);
        verify(mockApp).broadcastMessage(any());
    }

    private SecretSantaService mockedAppHandler(final SecretSantaBroadcastInput expectedInput, final SecretSantaBroadcastOutput stubbedOutput) {
        SecretSantaService mockAppHandler = mock(SecretSantaService.class);
        when(mockAppHandler.broadcastMessage(expectedInput)).thenReturn(stubbedOutput);

        return mockAppHandler;
    }
}