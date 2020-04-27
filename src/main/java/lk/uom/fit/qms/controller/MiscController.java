package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.service.AddressService;
import lk.uom.fit.qms.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/all-countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryDto>> getAllCountries(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(countryService.findAll(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Addresses")
    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressDto>> getAddresses(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(addressService.getAllAddress(search), HttpStatus.OK);
    }
}
