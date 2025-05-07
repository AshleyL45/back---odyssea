package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.dtos.ApiResponse;
import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.entities.mainTables.Option;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/options")
@Tag(name = "Options", description = "Handles all operations related to available options.")
public class OptionController {
    private final OptionDao optionDao;

    public OptionController(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    @Operation(
            summary = "Get all available options",
            description = "Returns a list of all available options in the system."
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Option>>> getAllOptions() {
        List<Option> options = optionDao.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Options retrieved successfully.", options, HttpStatus.OK)
        );
    }
}

