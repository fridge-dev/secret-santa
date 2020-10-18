package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceApiTestUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecretSantaServiceLambdaHandlerTest {

    @Test
    void callsAppLayer() throws Exception {
        // -- setup --
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();
        SecretSantaBroadcastInput appInput = ServiceApiTestUtils.makeInput();
        SecretSantaBroadcastOutput appOutput = ImmutableSecretSantaBroadcastOutput.builder()
                .failedPersonIds(Collections.emptySet())
                .build();
        SecretSantaService mockApp = mockedAppHandler(appInput, appOutput);

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockApp);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertNotNull(lambdaReply);
        verify(mockApp).broadcastMessage(any());
    }

    private SecretSantaService mockedAppHandler(final SecretSantaBroadcastInput expectedInput, final SecretSantaBroadcastOutput stubbedOutput) throws Exception {
        SecretSantaService mockAppHandler = mock(SecretSantaService.class);
        when(mockAppHandler.broadcastMessage(expectedInput)).thenReturn(stubbedOutput);

        return mockAppHandler;
    }
}