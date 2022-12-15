package com.frj.secretsanta.app;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.frj.secretsanta.app.api.Exclusion;
import com.frj.secretsanta.app.api.ImmutableSecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceApiTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultSecretSantaServiceTest {

    private SecretSantaService service;
    private AmazonSNS mockSns;
    private ArgumentCaptor<PublishRequest> publishRequestArgumentCaptor;

    @BeforeEach
    void setup() {
        mockSns = mock(AmazonSNS.class);
        publishRequestArgumentCaptor = ArgumentCaptor.forClass(PublishRequest.class);

        AppExternalDeps appExternalDeps = ImmutableAppExternalDeps.builder()
                .amazonSNS(mockSns)
                .build();
        AppModule appModule = AppModule.newInstance(appExternalDeps);

        service = appModule.getSecretSantaService();
    }

    @Test
    void broadcast_sanityTest() throws Exception {
        // -- setup --
        when(mockSns.publish(publishRequestArgumentCaptor.capture())).thenReturn(new PublishResult());
        SecretSantaBroadcastInput input = ImmutableSecretSantaBroadcastInput.builder()
                .messageFormat("{self.id} -> {target.id}")
                .personIdsToMessage(Arrays.asList("p1", "p2", "p4", "p5"))
                .people(Arrays.asList(
                        ServiceApiTestUtils.makePerson("p1", "1111", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p2", "2222", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p3", "3333", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p4", "4444", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p5", "5555", new HashMap<>())
                ))
                .exclusions(Arrays.asList(
                        Exclusion.of("p1", "p2"),
                        Exclusion.of("p3", "p4")
                ))
                .rngSeed(100L)
                .build();

        // -- execute --
        SecretSantaBroadcastOutput output = service.broadcastMessage(input);

        // -- verify --
        assertNotNull(output);
        assertTrue(output.failedPersonIds().isEmpty());
        verify(mockSns, times(4)).publish(any());

        final List<PublishRequest> snsCalls = publishRequestArgumentCaptor.getAllValues();
        Map<String, String> payloadByPhoneNumber = new HashMap<>();
        for (PublishRequest snsCall : snsCalls) {
            payloadByPhoneNumber.put(snsCall.getPhoneNumber(), snsCall.getMessage());
        }
        assertEquals("p1 -> p5", payloadByPhoneNumber.get("1111"));
        assertEquals("p2 -> p3", payloadByPhoneNumber.get("2222"));
        // skipped: p3 -> p1
        assertEquals("p4 -> p2", payloadByPhoneNumber.get("4444"));
        assertEquals("p5 -> p4", payloadByPhoneNumber.get("5555"));
    }

    @Test
    void broadcast_debugFromFile() throws Exception {
        // -- setup --
        when(mockSns.publish(publishRequestArgumentCaptor.capture())).thenReturn(new PublishResult());
        SecretSantaBroadcastInput input = ImmutableSecretSantaBroadcastInput.builder()
                .messageFormat("{self.id} -> {target.id}")
                .personIdsToMessage(Arrays.asList("p1", "p2", "p4", "p5"))
                .people(Arrays.asList(
                        ServiceApiTestUtils.makePerson("p1", "1111", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p2", "2222", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p3", "3333", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p4", "4444", new HashMap<>()),
                        ServiceApiTestUtils.makePerson("p5", "5555", new HashMap<>())
                ))
                .exclusions(Arrays.asList(
                        Exclusion.of("p1", "p2"),
                        Exclusion.of("p3", "p4")
                ))
                .rngSeed(100L)
                .build();

        // -- execute --
        SecretSantaBroadcastOutput _output = service.broadcastMessage(input);

        // -- verify --
        // Up to you! Attach debugger.
    }
}