package com.frj.secretsanta.app.internal;

import com.amazonaws.services.sns.AmazonSNS;
import org.immutables.value.Value;

@Value.Immutable
public interface AppExternalDeps {

    AmazonSNS amazonSNS();

}
