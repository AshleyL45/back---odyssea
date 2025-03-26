package com.example.odyssea.controllers.mainTables;

import com.example.odyssea.daos.mainTables.OptionDao;
import com.example.odyssea.entities.mainTables.Option;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/allById")
    public ResponseEntity<List<Option>> getOptionsById(@RequestParam List<Integer> ids){
        List<Option> options = optionDao.findOptionsByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(options);
    }

    @GetMapping("/price")
    public ResponseEntity<Option> getPrice(@PathVariable int optionId){
        Option option = optionDao.findPrice(optionId);
        return ResponseEntity.status(HttpStatus.OK).body(option);
    }
}

