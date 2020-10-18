package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceApiTestUtils;
import com.frj.secretsanta.app.internal.assignment.SecretSantaAssigner;
import com.frj.secretsanta.app.internal.sms.SmsMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DefaultSecretSantaServiceTest {

    private DefaultSecretSantaService service;
    private SecretSantaAssigner mockAssigner;
    private SmsMessenger mockMessenger;

    @BeforeEach
    void setup() {
        mockAssigner = mock(SecretSantaAssigner.class);
        mockMessenger = mock(SmsMessenger.class);
        service = new DefaultSecretSantaService(mockAssigner, mockMessenger);
    }

    @Test
    void broadcast() throws Exception {
        SecretSantaBroadcastOutput output = service.broadcastMessage(ServiceApiTestUtils.makeInput());

        assertNotNull(output);
    }
}