package com.frj.secretsanta.app;

import com.amazonaws.services.sns.AmazonSNS;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceApiTestUtils;
import com.frj.secretsanta.app.internal.AppExternalDeps;
import com.frj.secretsanta.app.internal.ImmutableAppExternalDeps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DefaultSecretSantaServiceTest {

    private SecretSantaService service;
    private AmazonSNS mockSns;

    @BeforeEach
    void setup() {
        mockSns = mock(AmazonSNS.class);

        AppExternalDeps appExternalDeps = ImmutableAppExternalDeps.builder()
                .amazonSNS(mockSns)
                .build();
        AppModule appModule = AppModule.newInstance(appExternalDeps);

        service = appModule.getSecretSantaService();
    }

    @Test
    void broadcast() throws Exception {
        SecretSantaBroadcastOutput output = service.broadcastMessage(ServiceApiTestUtils.makeInput());

        assertNotNull(output);
    }
}