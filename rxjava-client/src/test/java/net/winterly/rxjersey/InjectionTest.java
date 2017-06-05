package net.winterly.rxjersey;

import net.winterly.rxjersey.client.inject.Remote;
import org.junit.Test;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class InjectionTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(Resource.class);
    }

    @Test
    public void shouldReturnContent() {
        ResourceAPI resource = resource(ResourceAPI.class);
        String message = resource.inject("hello").toBlocking().first();

        assertEquals("hello", message);
    }

    @Path("/observable")
    public interface ResourceAPI {

        @GET
        @Path("echo")
        Observable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("inject")
        Observable<String> inject(@QueryParam("message") String message);

    }

    @Path("/observable")
    public static class Resource {

        @Remote
        private ResourceAPI remote;

        @Remote
        private WebTarget webTarget;

        @GET
        @Path("echo")
        public String echo(@QueryParam("message") String message) {
            return message;
        }

        @GET
        @Path("inject")
        public String inject(@QueryParam("message") String message) {
            return remote.echo(message).toBlocking().first();
        }

    }
}
