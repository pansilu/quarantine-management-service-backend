package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.dto.RegimentResponseDto;
import lk.uom.fit.qms.dto.UnitDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.HospitalService;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.util.enums.Rank;
import lk.uom.fit.qms.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/misc")
@Api(value = "Miscellaneous", tags = {"Hospital Details"})
public class MiscController extends  BaseController{

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ReportUserService reportUserService;

    @GetMapping(value = "/all-hospitals", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get All Hospitals")
    public ResponseEntity<List<HospitalDto>> getAllHosptials() {
        return new ResponseEntity<>( hospitalService.findHospitals(), HttpStatus.OK);
    }

    @GetMapping(value = "/all-countries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return new ResponseEntity<>( countryService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Ranks")
    @GetMapping(value = "/ranks",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = ArrayList.class)
    })
    public ResponseEntity getAllRanks() throws QmsException {

        return new ResponseEntity<>(Rank.values(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Roles")
    @GetMapping(value = "/roles",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = ArrayList.class)
    })
    public ResponseEntity getAllRoles() throws QmsException {

        return new ResponseEntity<>(RoleType.values(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All regiments")
    @GetMapping(value = "/regiments",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = ArrayList.class)
    })
    public ResponseEntity<List<RegimentResponseDto>> getAllRegiments() throws QmsException {

        List<RegimentResponseDto> regiments = reportUserService.getRegiments();
        return new ResponseEntity<List<RegimentResponseDto>>(regiments, HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Units")
    @GetMapping(value = "/units",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = ArrayList.class)
    })
    public ResponseEntity<List<UnitDto>> getAllUnits() throws QmsException {

        List<UnitDto> units = reportUserService.getUnits();
        return new ResponseEntity<>(units, HttpStatus.OK);
    }
}
