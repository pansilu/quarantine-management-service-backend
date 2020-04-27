package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.QuarantineCenterDto;
import lk.uom.fit.qms.dto.SuccessResponse;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.QuarantineCenterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.controller
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@RestController
@RequestMapping("/api/quarantine/center")
@Api(value = "Quarantine Center", tags = {"Quarantine Center Details"})
public class QuarantineCenterController extends BaseController{

    @Autowired
    private QuarantineCenterService quarantineCenterService;

    @ApiOperation(value = "Get All Quarantine Centers")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuarantineCenterDto>> getAllQuarantineCenter(@RequestParam(required = false) String search) {
        return new ResponseEntity<>(quarantineCenterService.findQuarantineCenters(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Create/Edit a Quarantine Center")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createOrEditQuarantineCenter(
            @Valid @RequestBody QuarantineCenterDto quarantineCenterDto, BindingResult bindingResult) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Quarantine Center create/edit validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.RUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        quarantineCenterService.createOrEditQuarantineCenter(quarantineCenterDto);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Quarantine Center Details")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineCenterDto> getQuarantineCenter(@PathVariable("id") Long hospitalId) throws QmsException {
        return new ResponseEntity<>(quarantineCenterService.getQuarantineCenterDetails(hospitalId), HttpStatus.OK);
    }
}
