package lk.uom.fit.qms.controller;

import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.dto.RegimentResponseDto;
import lk.uom.fit.qms.dto.UnitDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.service.AddressService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/misc")
@Api(value = "Miscellaneous", tags = {"Utils"})
public class MiscController extends  BaseController{


    @Autowired
    private AddressService addressService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ReportUserService reportUserService;



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

    @ApiOperation(value = "Get Ranks")
    @GetMapping(value = "/ranks",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = ArrayList.class)
    })
    public ResponseEntity getAllRanks(
            @ApiParam(value = "Type Should be officers, others or all")
            @RequestParam(value = "type", required = false, defaultValue = "all") String type
    ) throws QmsException {

        List<Rank> ranks = new ArrayList<>();

        if(type.toLowerCase().equals("officers")){
            ranks = Rank.getOfficers();
        }else if(type.toLowerCase().equals("others")){
            ranks = Rank.getOthers();
        }else if(type.toLowerCase().equals("all")){
            ranks = Rank.getAll();
        }

        return new ResponseEntity<>(ranks, HttpStatus.OK);
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
