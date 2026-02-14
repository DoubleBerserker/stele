package io.github.DoubleBerserker.stele.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Category cannot contain any special characters!")
    private String name;
}
