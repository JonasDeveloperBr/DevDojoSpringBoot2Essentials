package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static academy.devdojo.springboot2.constants.MessageConstants.*;

@Log4j2
public class SpringClient {

    public static void main(String[] args) {

        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(URL_BASIC + KEY_ID, Anime.class, 59);
        log.info(entity);

        Anime object = new RestTemplate().getForObject(URL_BASIC + KEY_ID, Anime.class, 59);
        log.info(object);

        Anime[] animes = new RestTemplate().getForObject(URL_BASIC, Anime[].class);
        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(
                URL_BASIC + "all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        log.info(exchange.getBody());

        Anime samuraiChamplo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange(
                URL_BASIC,
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamplo),
                Anime.class);
        log.info(SAVED_ANIME, samuraiChamplooSaved);

        Anime animeToBeUpdated = samuraiChamplooSaved.getBody();
        animeToBeUpdated.setName("Samurai Champloo 2");

        ResponseEntity<Void> samuraiChamploUpdated = new RestTemplate().exchange(URL_BASIC,
                HttpMethod.PUT,
                new HttpEntity<>(animeToBeUpdated, createJsonHeader()),
                Void.class);
        log.info(samuraiChamploUpdated);

        ResponseEntity<Void> samuraiChamplooDeleted = new RestTemplate().exchange(URL_BASIC + KEY_ID,
                HttpMethod.DELETE,
                null,
                Void.class,
                animeToBeUpdated.getId());
        log.info(samuraiChamplooDeleted);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
