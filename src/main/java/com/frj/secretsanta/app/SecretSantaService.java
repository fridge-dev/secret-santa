package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.ClientException;
import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;
import com.frj.secretsanta.app.api.ServiceException;

public interface SecretSantaService {

    SecretSantaBroadcastOutput broadcastMessage(SecretSantaBroadcastInput input) throws ClientException, ServiceException;

}
