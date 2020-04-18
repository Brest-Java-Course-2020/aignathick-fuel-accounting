package com.epam.brest.courses.web_app;

import com.epam.brest.courses.model.Fuel;
import com.epam.brest.courses.model.Transport;
import com.epam.brest.courses.service.FuelService;
import com.epam.brest.courses.service.TransportService;
import com.epam.brest.courses.util.DateUtilites;
import com.epam.brest.courses.web_app.validators.TransportDateValidator;
import com.epam.brest.courses.web_app.validators.TransportNameValidator;
import com.epam.brest.courses.web_app.validators.TransportTankCapasityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

/**
 * Transport MVC controller.
 */
@Controller
public class TransportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportController.class);
    private final TransportService transportService;
    private final FuelService fuelService;
    @Autowired
    private TransportNameValidator transportNameValidator;
    @Autowired
    private TransportTankCapasityValidator transportTankCapasityValidator;
    @Autowired
    private TransportDateValidator transportDateValidator;

    public TransportController(TransportService transportService, FuelService fuelService) {
        this.transportService = transportService;
        this.fuelService = fuelService;
    }

    /**
     *  Goto transports list page.
     * @param model model.
     * @return view name.
     */
    @GetMapping(value = "/transports")
    public String transports(Model model) {
        LOGGER.debug("transports()");
        model.addAttribute("transports", this.transportService.findAll());
        model.addAttribute("fuelsMap", getFuelsMap(this.fuelService.findAll()));
        model.addAttribute("dateFrom", DateUtilites.getMonthStartDate());
        model.addAttribute("dateTo", DateUtilites.getMonthEndDate());
        return "transports";
    }

    /**
     * Show transports data by date filter.
     *
     * @param dateFrom date from.
     * @param dateTo date to.
     * @param model model.
     * @return view name.
     */
    @GetMapping(value = "/transports/from/{dateFrom}/to/{dateTo}")
    public  String findTransportsByDates(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date dateFrom,
                                         @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date dateTo,
                                         Model model){
        LOGGER.debug("findTransportsByDates({},{},{})", dateFrom, dateTo, model);
        model.addAttribute("fuelsMap", getFuelsMap(this.fuelService.findAll()));
        model.addAttribute("transports",
                this.transportService.findAllFromDateToDate(dateFrom, dateTo));
        return "transports";
    }

    /**
     * Show transports data by date filter.
     *
     * @param dateFrom date from.
     * @param dateTo date to.
     * @param model model.
     * @return view name.
     */
    @PostMapping(value = "/transports/filter")
    public  String findTransportsByDatesPost(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(name = "dateFrom") Date dateFrom,
                                             @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(name = "dateTo") Date dateTo,
                                             Model model){
        LOGGER.debug("findTransportsByDates({},{},{})", dateFrom, dateTo, model);
        model.addAttribute("fuelsMap", getFuelsMap(this.fuelService.findAll()));
        model.addAttribute("transports",
                this.transportService.findAllFromDateToDate(dateFrom, dateTo));
        model.addAttribute("dateFrom", DateUtilites.getMonthStartDate());
        model.addAttribute("dateTo", DateUtilites.getMonthEndDate());
        return "transports";
    }

    /**
     *  Goto edit transport page.
     *
     * @param id transport id.
     * @param model model.
     * @return view name.
     */
    @GetMapping(value = "/transport/{id}")
    public String gotoEditTransportPage(@PathVariable Integer id, Model model){
        LOGGER.debug("gotoEditTransportPage({},{})", id, model);
        Optional<Transport> transportOptional
                = this.transportService.findById(id);
        if (transportOptional.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("transport", transportOptional.get());
            model.addAttribute("fuels", this.fuelService.findAll());
            return "transport";
        } else {
            return "redirect:/transports";
        }
    }

    /**
     * Update transport.
     * @param transport transport with filled data.
     * @param result binding result.
     * @return view name.
     */
    @PostMapping(value = "/transport/{id}")
    public String updateTransport(Model model,
                                  @Valid Transport transport,
                                  BindingResult result){
        LOGGER.debug("updateTransport({}, {}, {})", model, transport, result);
        transportNameValidator.validate(transport, result);
        transportDateValidator.validate(transport, result);
        transportTankCapasityValidator.validate(transport, result);
        if (result.hasErrors()){
                model.addAttribute("isNew", false);
                model.addAttribute("transport", transport);
                model.addAttribute("fuels", this.fuelService.findAll());
                return "transport";
        } else {
            this.transportService.update(transport);
            return "redirect:/transports";
        }
    }

    /**
     * Goto add transport page.
     *
     * @param model Model.
     * @return view name.
     */
    @GetMapping(value = "/transport")
    public String gotoAddTransportPage(Model model){
        LOGGER.debug("gotoAddTransportPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("transport", new Transport().setFuelId(1));
        model.addAttribute("fuels", this.fuelService.findAll());
        return "transport";
    }

    /**
     * Persist new transport into persistence storage.
     *
     * @param transport new transport with filled data.
     * @return view name.
     */
    @PostMapping(value = "/transport")
    public String addTransport(Transport transport){
        LOGGER.debug("addTransport({})", transport);
        this.transportService.create(transport);
        return "redirect:/transports";
    }

    /**
     * Delete transport by TransportId.
     *
     * @param id TransportId.
     * @param model model.
     * @return view name.
     */
    @GetMapping(value = "/transport/{id}/delete")
    public String deleteTransportById(@PathVariable Integer id, Model model){
        LOGGER.debug("deleteTransportById({}{})", id, model);
        this.transportService.delete(id);
        return "redirect:/transports";
    }

    /**
     * Get fuels map for thymeleaf template.
     *
     * @param fuels List of fuels.
     * @return HasMap<Integer, Fuel>.
     */
    private Map<Integer, Fuel> getFuelsMap(List<Fuel> fuels) {
        Map<Integer, Fuel> fuelsMap = new HashMap<Integer, Fuel>();
        for (Fuel fuel : fuels) {
            fuelsMap.put(fuel.getFuelId(), fuel);
        }
        return fuelsMap;
    }
}
