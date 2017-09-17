package hfs.de.rhinov2.update;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by study on 16.09.17.
 */

public interface UpdateRESTService {
    @POST("update")
    @FormUrlEncoded
    Call<JsonObject> getUpdates(@Field("latitude") double latitude, @Field("longitude") double longitude);
}
