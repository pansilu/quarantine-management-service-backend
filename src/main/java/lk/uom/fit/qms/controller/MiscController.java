package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.service.AddressService;
import lk.uom.fit.qms.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/misc")
@Api(value = "Miscellaneous", tags = {"Misc Details"})
public class MiscController extends BaseController{

    private final CountryService countryService;

    private final AddressService addressService;

    @Autowired
    public MiscController(CountryService countryService, AddressService addressService) {
        this.countryService = countryService;
        this.addressService = addressService;
    }

    @ApiOperation(value = "Get Countries")
    @GetMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryDto>> getAllCountries(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(countryService.findAll(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Addresses")
    @GetMapping(value = "/address/gnd/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressDto>> getAddresses(
            @PathVariable("id") Long gndId,
            @RequestParam(required = false) String line,
            @RequestParam(required = false) String police) throws QmsException {

        return new ResponseEntity<>(addressService.getAllAddress(gndId, police, line), HttpStatus.OK);
    }
}
