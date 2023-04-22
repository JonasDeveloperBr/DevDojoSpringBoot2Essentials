package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import static academy.devdojo.springboot2.constants.MessageConstants.*;

@Data
@Builder
public class AnimePutRequestBody {
    @NotEmpty(message = THE_ANIME_ID_CANNOT_BE_EMPTY)
    @Schema(description = THIS_IS_THE_ANIME_S_ID)
    private Long id;

    @NotEmpty(message = ANIME_NAME_CANNOT_BE_EMPTY)
    @Schema(description = THIS_IS_THE_ANIME_S_NAME)
    private String name;
}