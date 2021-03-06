package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxMethodDispatcher;
import net.winterly.rxjersey.server.RxMethodDispatcherProvider;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class SingleMethodDispatcher extends RxMethodDispatcher {

    @Context
    private javax.inject.Provider<ContainerRequestContext> containerRequestContext;

    @Context
    private IterableProvider<ObservableRequestInterceptor<?>> requestInterceptors;

    public SingleMethodDispatcher(ResourceMethodDispatcher originalDispatcher) {
        super(originalDispatcher);
    }

    @Override
    public void dispatch(AsyncContext asyncContext, ResourceMethodDispatcher dispatcher, Object resource, ContainerRequest request) throws ProcessingException {
        final ContainerRequestContext requestContext = containerRequestContext.get();

        Completable intercept = Observable.from(requestInterceptors)
                .concatMap(interceptor -> interceptor.intercept(requestContext))
                .lastOrDefault(null)
                .toCompletable();

        Single<?> dispatch = Single.defer(() -> (Single<?>) dispatcher.dispatch(resource, request).getEntity());

        intercept.andThen(dispatch)
                .map(response -> response == null ? Response.noContent().build() : response)
                .subscribe(asyncContext::resume, asyncContext::resume);
    }

    public static class Provider extends RxMethodDispatcherProvider {

        @Inject
        public Provider(ServiceLocator serviceLocator) {
            super(serviceLocator, Observable.class, Single.class, Completable.class);
        }

        @Override
        public RxMethodDispatcher create(ResourceMethodDispatcher dispatcher) {
            return new SingleMethodDispatcher(dispatcher);
        }
    }
}
