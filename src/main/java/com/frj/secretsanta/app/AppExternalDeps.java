package com.frj.secretsanta.app;

import com.amazonaws.services.sns.AmazonSNS;
import org.immutables.value.Value;

@Value.Immutable
public interface AppExternalDeps {

    AmazonSNS amazonSNS();

}
