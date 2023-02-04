package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 59);
        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 59);
        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(
                "http://localhost:8080/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        log.info(exchange.getBody());

//        Anime kingdom = Anime.builder().name("kingdom").build();
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", kingdom, Anime.class);
//        log.info("saved anime {}", kingdomSaved);
//
//        Anime anime = new Anime();
//        anime.setName("United");
//        Anime united = new RestTemplate().postForObject("http://localhost:8080/animes/", anime, Anime.class);
//        log.info("saved anime {}", united);

        Anime samuraiChamplo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange(
                "http://localhost:8080/animes/",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamplo),
                Anime.class);
        log.info("saved anime {}", samuraiChamplooSaved);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
