package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.model.Country;
import lk.uom.fit.qms.model.Hospital;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/misc")
@Api(value = "Miscellaneous", tags = {"Hospital Details"})
public class MiscController extends  BaseController{

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/all-hospitals", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get All Hospitals")
    public ResponseEntity<List<Hospital>> getAllHosptials() {
        return new ResponseEntity<>( hospitalService.findHospitals(), HttpStatus.OK);
    }

    @GetMapping(value = "/all-countries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        return new ResponseEntity<>( countryService.findAll(), HttpStatus.OK);
    }
}
