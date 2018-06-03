package net.winterly.rxjersey.example;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public interface GithubResource {

    @GET
    @Path("github")
    Single<GithubRepository> getRepository();

    @GET
    @Path("echo/{message}")
    Single<String> echo(@PathParam("message") String message);

    @GET
    @Path("echo/observable/{message}")
    Observable<String> echoObservable(@PathParam("message") String message);

    @GET
    @Path("echo/maybe/{message}")
    Maybe<String> echoMaybe(@PathParam("message") String message);

    @GET
    @Path("echo/flowable/{message}")
    Flowable<String> echoFlowable(@PathParam("message") String message);
}