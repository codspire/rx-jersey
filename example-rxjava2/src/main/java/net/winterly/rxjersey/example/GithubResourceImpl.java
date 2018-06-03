package net.winterly.rxjersey.example;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import net.winterly.rxjersey.client.inject.Remote;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public class GithubResourceImpl implements GithubResource {

    @Remote("https://api.github.com")
    private GithubApi githubApi;

    @Override
    public Single<GithubRepository> getRepository() {
        return Single.fromObservable(githubApi.getRepository("alex-shpak", "rx-jersey"));
    }

    @Override
    public Single<String> echo(String message) {
        return Single.just("Single:: " + message);
    }

    @Override
    public Observable<String> echoObservable(String message) {
        return Observable.just("Observable:: " + message);
    }

    @Override
    public Maybe<String> echoMaybe(String message) {
        return Maybe.just("Maybe:: " + message);
    }

    @Override
    public Flowable<String> echoFlowable(String message) {
        return Flowable.just("Flowable:: " + message);
    }
}
