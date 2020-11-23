package com.frj.secretsanta.lambda;

import com.frj.secretsanta.app.SecretSantaService;
import com.frj.secretsanta.app.api.Exclusion;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceApiTestUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecretSantaServiceLambdaHandlerTest {

    @Test
    void callsAppLayer() throws Exception {
        // -- setup --
        SecretSantaLambdaRequest lambdaRequest = new SecretSantaLambdaRequest();
        lambdaRequest.setMessageFormat("Hello");
        lambdaRequest.setPersonIdsToMessage(null);
        lambdaRequest.setPeopleData(Arrays.asList(
                Map.of("id", "p1", "phoneNumber", "1111", "k1a", "v1a", "k1b", "v1b"),
                Map.of("id", "p2", "phoneNumber", "2222", "k2a", "v2a", "k2b", "v2b"),
                Map.of("id", "p3", "phoneNumber", "3333", "k3a", "v3a", "k3b", "v3b")
        ));
        lambdaRequest.setExclusionRules(Collections.singletonList(new String[]{"p1", "p2"}));
        lambdaRequest.setRngSeed(1L);

        SecretSantaBroadcastOutput appOutput = ImmutableSecretSantaBroadcastOutput.builder()
                .failedPersonIds(Collections.emptySet())
                .build();
        SecretSantaService mockAppHandler = mock(SecretSantaService.class);
        when(mockAppHandler.broadcastMessage(any())).thenReturn(appOutput);

        // -- execute --
        SecretSantaLambdaHandler lambdaHandler = new SecretSantaLambdaHandler(mockAppHandler);
        SecretSantaLambdaReply lambdaReply = lambdaHandler.handleRequest(lambdaRequest, new NullLambdaContext());

        // -- verify --
        assertEquals("200", lambdaReply.getStatus());

        SecretSantaBroadcastInput expectedAppInput = ImmutableSecretSantaBroadcastInput.builder()
                .messageFormat("Hello")
                .personIdsToMessage(Arrays.asList("p1", "p2", "p3"))
                .people(Arrays.asList(
                        ServiceApiTestUtils.makePerson("p1", "1111", Map.of("k1a", "v1a", "k1b", "v1b")),
                        ServiceApiTestUtils.makePerson("p2", "2222", Map.of("k2a", "v2a", "k2b", "v2b")),
                        ServiceApiTestUtils.makePerson("p3", "3333", Map.of("k3a", "v3a", "k3b", "v3b"))
                ))
                .addExclusions(Exclusion.of("p1", "p2"))
                .rngSeed(1L)
                .build();
        verify(mockAppHandler).broadcastMessage(expectedAppInput);
    }
}