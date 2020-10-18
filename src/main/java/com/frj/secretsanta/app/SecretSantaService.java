package com.frj.secretsanta.app;

import com.frj.secretsanta.app.api.SecretSantaBroadcastInput;
import com.frj.secretsanta.app.api.SecretSantaBroadcastOutput;

public interface SecretSantaService {

    SecretSantaBroadcastOutput broadcastMessage(SecretSantaBroadcastInput input);

}
