package dev.example.demonet.grpc.service;

import static com.google.rpc.Code.ALREADY_EXISTS;
import static com.google.rpc.Code.NOT_FOUND;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

import com.google.rpc.Code;
import com.google.rpc.Status;

import dev.example.demonet.service.exception.DuplicateProjectException;
import dev.example.demonet.service.exception.EntityNotFoundException;
import dev.example.demonet.service.exception.ProjectManagementException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ProjectManagementExceptionHandler {

    @GrpcExceptionHandler(EntityNotFoundException.class)
    public StatusRuntimeException handleEntityNotFoundException(EntityNotFoundException cause) {
        return toStatusRuntimeException(getExceptionStatus(cause, NOT_FOUND));
    }

    @GrpcExceptionHandler(DuplicateProjectException.class)
    public StatusRuntimeException handleDuplicateProjectException(DuplicateProjectException cause) {
        return toStatusRuntimeException(getExceptionStatus(cause, ALREADY_EXISTS));
    }

    private Status getExceptionStatus(ProjectManagementException cause, Code code) {
        return Status.newBuilder().setCode(code.getNumber()).setMessage(cause.getMessage()).build();
    }
}
