package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.service.AddressService;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.HospitalService;

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
public class MiscController extends  BaseController{

    private final HospitalService hospitalService;

    private final CountryService countryService;

    private final AddressService addressService;

    @Autowired
    public MiscController(HospitalService hospitalService, CountryService countryService, AddressService addressService) {
        this.hospitalService = hospitalService;
        this.countryService = countryService;
        this.addressService = addressService;
    }

    @GetMapping(value = "/all-hospitals", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get All Hospitals")
    public ResponseEntity<List<HospitalDto>> getAllHosptials(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(hospitalService.findHospitals(search), HttpStatus.OK);
    }

    @GetMapping(value = "/all-countries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Countries")
    public ResponseEntity<List<CountryDto>> getAllCountries(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(countryService.findAll(search), HttpStatus.OK);
    }

    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Addresses")
    public ResponseEntity<List<AddressDto>> getAddresses(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(addressService.getAllAddress(search), HttpStatus.OK);
    }
}
