package com.example.odyssea.controllers;

import com.example.odyssea.exceptions.ErrorResponse;
import com.example.odyssea.exceptions.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(responseCode = "200", description = "Success",
                        content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
                @ApiResponse(responseCode = "400", description = "Validation error",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
)

public @interface QuestionnaireResponses {

}
