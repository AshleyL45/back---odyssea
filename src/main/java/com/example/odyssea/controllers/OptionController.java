package com.example.odyssea.controllers;

import com.example.odyssea.daos.OptionDao;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/options")
public class OptionController {
    private final OptionDao optionDao;

    public OptionController(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Option>> getAllOptions(){
        List<Option> options = optionDao.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(options);
    }

    @GetMapping("/price")
    public ResponseEntity<Option> getPrice(@PathVariable int optionId){
        Option option = optionDao.findPrice(optionId);
        return ResponseEntity.status(HttpStatus.OK).body(option);
    }
}

