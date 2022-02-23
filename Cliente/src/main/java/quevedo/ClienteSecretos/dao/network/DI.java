package quevedo.ClienteSecretos.dao.network;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import quevedo.ClienteSecretos.config.ConfigurationClient;
import quevedo.ClienteSecretos.dao.retrofit.RetrofitUsuarios;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DI {



    @Produces
    @Singleton
    public OkHttpClient getOKHttpClient(AuthoritationInterceptor authoritationInterceptor) {
        return new OkHttpClient.Builder()
                .protocols(List.of(Protocol.HTTP_1_1))
                .readTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .callTimeout(Duration.of(10, ChronoUnit.MINUTES))
                .connectTimeout(Duration.of(10, ChronoUnit.MINUTES))
//                .addInterceptor(authoritationInterceptor)
                .build();
    }

    @Produces
    @Singleton
    @Named(ConstantesDI.PATHBASE)
    public String getPathBase(ConfigurationClient configurationClient) {
        return configurationClient.getPathbase();
    }

    @Produces
    @Singleton
    public Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.toString()))
                .create();
    }

    @Produces
    @Singleton
    public Retrofit createRetrofit(OkHttpClient client, @Named(ConstantesDI.PATHBASE) String pathBase) {
        return new Retrofit.Builder()
                .baseUrl(pathBase)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Produces
    public RetrofitUsuarios apiUsuarios (Retrofit retrofit){
        return retrofit.create(RetrofitUsuarios.class);
    }

}
