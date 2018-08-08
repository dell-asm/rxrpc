/**
 *
 */
package com.slimgears.rxrpc.server;

import com.slimgears.rxrpc.core.data.Path;
import org.reactivestreams.Publisher;

import java.util.Map;
import java.util.Optional;

import static com.slimgears.rxrpc.server.EndpointDispatchers.EMPTY;

class CompositeEndpointDispatcher implements EndpointDispatcher {
    private final EndpointResolver resolver;
    private final Map<String, Factory> dispatcherMap;

    public CompositeEndpointDispatcher(EndpointResolver resolver, Map<String, Factory> dispatcherMap) {
        this.resolver = resolver;
        this.dispatcherMap = dispatcherMap;
    }

    @Override
    public Publisher<?> dispatch(String path, InvocationArguments args) {
        Path p = Path.of(path);
        return Optional
                .ofNullable(dispatcherMap.get(p.head()))
                .map(factory -> factory.create(resolver))
                .orElse(EMPTY)
                .dispatch(p.tail(), args);
    }
}
