package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static academy.devdojo.springboot2.constants.MessageConstants.*;

@RestController
@RequestMapping("/v1/animes")
@Log4j2
@RequiredArgsConstructor
@Tag(name = "Animes", description = "Anime Manipulations")
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    @Operation(
            summary = "Retrieve all Animes paginated",
            description = "The default size is 20, use the parameter size to change the default value",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(
            summary = "Retrieve all Animes",
            description = "No pagination and no filter",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<List<Anime>> listAll() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Find Anime by Id",
            description = "Return an Anime",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_400, description = ANIME_NOT_FOUND),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    //TODO remove this endPoint and implement @AuthenticationPrincipal in all other endPoints
    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(
            @PathVariable long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @Operation(
            summary = "Find Anime by Name",
            description = "Retrieve a list of Anime",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_400, description = ANIME_NOT_FOUND),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        //TODO create an exception when Anime not found and return Http 400
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PostMapping
    @Operation(
            summary = "Create a new Anime",
            description = "Add new Anime",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HTTP_201,
                    description = SUCCESSFUL_OPERATION,
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnimePostRequestBody.class))
                    }),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED),
            @ApiResponse(responseCode = HTTP_400, description = BAD_REQUEST_EXCEPTION_INVALID_FIELDS)
    })
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(
            summary = "Delete an existing Anime",
            description = "Delete Anime by Id",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_204, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_400, description = WHEN_ANIME_DOESN_T_EXIST_IN_THE_DATABASE),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(
            summary = "Update an existing Anime",
            description = "Update Anime by Id",
            security = {@SecurityRequirement(name = "BasicAuthentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_204, description = SUCCESSFUL_OPERATION),
            @ApiResponse(responseCode = HTTP_400, description = WHEN_ANIME_DOESN_T_EXIST_IN_THE_DATABASE),
            @ApiResponse(responseCode = HTTP_401, description = AUTHORIZATION_FAILED)
    })
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
