package doctalk.functionalreactive.Retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by anish wadhwa on 5/13/2017.
 */

public interface GitHubInterface {
    @GET("/repos/{owner}/{repo}/issues")
    Observable<List<Issue>> issues(
            @Path("owner") String owner, @Path("repo") String repo);
}
