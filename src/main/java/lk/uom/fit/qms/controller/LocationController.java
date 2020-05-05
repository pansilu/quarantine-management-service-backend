package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.DistrictResDto;
import lk.uom.fit.qms.dto.DivisionResDto;
import lk.uom.fit.qms.dto.GnDivisionResDto;
import lk.uom.fit.qms.dto.ProvinceResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.service.DistrictService;
import lk.uom.fit.qms.service.DivisionService;
import lk.uom.fit.qms.service.GramaNiladariDivisionService;
import lk.uom.fit.qms.service.ProvinceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.controller
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@RestController
@RequestMapping("/api/location")
@Api(value = "Location", tags = {"Location Details"})
public class LocationController extends BaseController {

    private final ProvinceService provinceService;

    private final DistrictService districtService;

    private final DivisionService divisionService;

    private final GramaNiladariDivisionService gramaNiladariDivisionService;

    @Autowired
    public LocationController(ProvinceService provinceService, DistrictService districtService, DivisionService divisionService, GramaNiladariDivisionService gramaNiladariDivisionService) {
        this.provinceService = provinceService;
        this.districtService = districtService;
        this.divisionService = divisionService;
        this.gramaNiladariDivisionService = gramaNiladariDivisionService;
    }

    @ApiOperation(value = "Get All Provinces")
    @GetMapping(value = "/province", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProvinceResDto>> getAllProvince(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(provinceService.findAllProvinces(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Districts in a Province")
    @GetMapping(value = "/province/{id}/district", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DistrictResDto>> getAllDistrictsInProvince(@PathVariable("id") Long provinceId, @RequestParam(required = false) String search) throws QmsException {
        return new ResponseEntity<>(districtService.getAllDistrictsInProvince(provinceId, search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All DS Divisions in a District")
    @GetMapping(value = "/district/{id}/division", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DivisionResDto>> getAllDivisionsInDistrict(@PathVariable("id") Long districtId, @RequestParam(required = false) String search) throws QmsException {
        return new ResponseEntity<>(divisionService.getAllDivisionsInDistrict(districtId, search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All GN Divisions in a Division")
    @GetMapping(value = "/division/{id}/gnd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GnDivisionResDto>> getAllGnDivisionsInDsDivision(@PathVariable("id") Long divisionId, @RequestParam(required = false) String search) throws QmsException {
        return new ResponseEntity<>(gramaNiladariDivisionService.getAllGnDivisionsInDsDivision(divisionId, search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get GN Division")
    @GetMapping(value = "/gnd/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GnDivisionResDto> getGnDivision(@PathVariable("id") Long gndId) throws QmsException {
        return new ResponseEntity<>(gramaNiladariDivisionService.getGnDivisionDetailsById(gndId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get District")
    @GetMapping(value = "/district/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DistrictResDto> getDistrict(@PathVariable("id") Long districtId) throws QmsException {
        return new ResponseEntity<>(districtService.getDistrictDetailsById(districtId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Districts")
    @GetMapping(value = "/district", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DistrictResDto>> getAllDistrict(@RequestParam(required = false) String search, @RequestParam(required = false) List<Long> districtIds) throws QmsException {
        return new ResponseEntity<>(districtService.getAllDistricts(search, districtIds), HttpStatus.OK);
    }
}
