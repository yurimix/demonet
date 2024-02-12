package dev.example.demonet.ws.endpoint;

import javax.xml.namespace.QName;

import org.springframework.http.HttpStatus;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import dev.example.demonet.service.exception.EntityDuplicateException;
import dev.example.demonet.service.exception.EntityNotFoundException;
import dev.example.demonet.service.exception.ProjectManagementException;

public class ProjectManagementExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName MESSAGE = new QName("message");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof ProjectManagementException) {
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(MESSAGE).addText(ex.getMessage());
            if (ex instanceof EntityNotFoundException) {
                detail.addFaultDetailElement(CODE).addText(HttpStatus.NOT_FOUND.name());
            }
            if (ex instanceof EntityDuplicateException) {
                detail.addFaultDetailElement(CODE).addText(HttpStatus.BAD_REQUEST.name());
            }
        }
    }

}
