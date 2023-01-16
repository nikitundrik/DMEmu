package net.lockethefirst.lockesdmemu.ui.home;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DNDService {
    @GET("/api/monsters/{index}")
    public Call<MonsterResponse> getMonster(@Path("index") String index);
}
