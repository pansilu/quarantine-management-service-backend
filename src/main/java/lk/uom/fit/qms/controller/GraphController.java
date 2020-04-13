package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.GraphRequestDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.GraphService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
 * @created on 4/11/2020
 * @Package lk.uom.fit.qms.controller
 * @company Axiata Digital Labs (pvt)Ltd.
 */
@RestController
@RequestMapping("/api/graph")
@Api(value = "graph", tags = {"Graph Detail Management"})
public class GraphController extends BaseController {

    private final GraphService graphService;

    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @ApiOperation(value = "Get graph details")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getGraphDetails(
            @Valid @RequestBody GraphRequestDto graphRequestDto, BindingResult bindingResult, HttpServletRequest request) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Graph details get validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.QUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);
        Object response = graphService.getGraphDetails(graphRequestDto, userId, userRoles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
