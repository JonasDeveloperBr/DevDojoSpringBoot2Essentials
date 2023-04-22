package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

import static academy.devdojo.springboot2.constants.MessageConstants.ANIME_NAME_CANNOT_BE_EMPTY;
import static academy.devdojo.springboot2.constants.MessageConstants.THIS_IS_THE_ANIME_S_NAME;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
    @NotEmpty(message = ANIME_NAME_CANNOT_BE_EMPTY)
    @Schema(description = THIS_IS_THE_ANIME_S_NAME, example = "Tensei Shittara Slime Datta Ken")
    private String name;

}