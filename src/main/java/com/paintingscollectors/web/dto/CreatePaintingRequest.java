package com.paintingscollectors.web.dto;

import com.paintingscollectors.painting.model.Style;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CreatePaintingRequest {
    @NotNull(message = "Name is required.")
    @Size(min = 5, max = 40, message = "Name length must be between 5 and 40 characters (inclusive of 5 and 40).")
    private String name;

    @NotNull(message = "Author is required.")
    @Size(min = 5, max = 30, message = "Author length must be between 5 and 30 characters (inclusive of 5 and 30).")
    private String author;

    @NotNull(message = "ImageUrl is required.")
    @URL(message = "Invalid url constraints.")
    private String imageUrl;

    @NotNull(message = "Style is required.")
    private Style style;
}
