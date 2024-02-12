package dev.example.demonet.graphql.controller;

import java.util.Optional;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import dev.example.demonet.service.exception.EntityDuplicateException;
import dev.example.demonet.service.exception.EntityNotFoundException;
import dev.example.demonet.service.exception.ProjectManagementException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {

        if (ex instanceof ProjectManagementException) {
            var builder = GraphqlErrorBuilder.newError()
                    .message(Optional.ofNullable(ex.getMessage()).orElse("UNKNOWN ERROR"))
                    .path(env.getExecutionStepInfo().getPath()).location(env.getField().getSourceLocation());
            if (ex instanceof EntityNotFoundException) {
                builder.errorType(ErrorType.NOT_FOUND);
            }
            if (ex instanceof EntityDuplicateException) {
                builder.errorType(ErrorType.BAD_REQUEST);
            }
            return builder.build();
        }
        return null;
    }
}
