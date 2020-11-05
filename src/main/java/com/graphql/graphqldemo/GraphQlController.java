package com.graphql.graphqldemo;


import graphql.ExecutionInput;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import graphql.GraphQL;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class GraphQlController {

        @Autowired
        private GraphQL graphql;

        @PostMapping(value="graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public Mono<Map<String,Object>> execute(@RequestBody GraphQlRequestBody body) {
                return Mono.fromCompletionStage(graphql.executeAsync(ExecutionInput.newExecutionInput().query(body.getQuery())
                        .operationName(body.getOperationName()).build()))
                        .map(ExecutionResult::toSpecification);
        }







}
