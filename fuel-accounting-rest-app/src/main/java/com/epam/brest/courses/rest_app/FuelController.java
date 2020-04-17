package com.epam.brest.courses.rest_app;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.rest_app.exception.FuelNotFoundException;
import com.epam.brest.courses.service.FuelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Fuel rest controller.
 */
@RestController
public class FuelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuelController.class);
    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    /**
     * Get all fuels.
     *
     * @return fuels list json.
     */
    @GetMapping(value = "/fuels")
    public ResponseEntity<List<Fuel>> fuels() {
        LOGGER.debug("fuels()");
        List<Fuel> result = fuelService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get fuel by id.
     *
     * @param id fuel id.
     * @return json fuel.
     */
    @GetMapping(value="/fuels/{id}")
    public Fuel findById(@PathVariable Integer id){
        LOGGER.debug("findById({})", id);
        return fuelService.findById(id).orElseThrow(() -> new FuelNotFoundException(id));
    }

    /**
     * Add new Fuel into DB.
     * @param fuel Fuel.
     * @return ResponseEntity<Integer> created fuel id and http status 200.
     */
    @PostMapping(path = "/fuels", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> createFuel(@RequestBody Fuel fuel){
        LOGGER.debug("createFuel({})", fuel);
        Integer id = fuelService.create(fuel);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Update fuel in DB.
     * @param fuel Fuel with data.
     * @return ResponseEntity<Integer> number of updated rows,
     */
    @PutMapping(path = "/fuels", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> updateFuel(@RequestBody Fuel fuel){
        LOGGER.debug("updateFuel({})", fuel);
        int result = fuelService.update(fuel);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete fuel from DB.
     * @param id fuel id.
     * @return ResponseEntity<Integer> number iof rows deleted.
     * */
    @DeleteMapping(path = "/fuels/{id}", produces = "application/json")
    public ResponseEntity<Integer> deleteFue(@PathVariable Integer id){
        LOGGER.debug("Delete({})", id);
        int result = fuelService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
