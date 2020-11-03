package com.graphql.graphqldemo;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import io.r2dbc.spi.ConnectionFactory;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class GraphQlFirstApplication {

	@Value("classpath:schema.graphql")
	Resource resource;

	@Autowired
	private BookService bookService;

	@Autowired
	private AuthorService authorService;

	public static void main(String[] args) {

		SpringApplication.run(GraphQlFirstApplication.class, args);
	}


	@Bean
	public ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory){

		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);

		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"),new ClassPathResource("data.sql"));
		initializer.setDatabasePopulator(populator);
		return initializer;
	}
	@Bean
	public GraphQL graphQL() throws IOException{
		File schemaFile = resource.getFile();
		SchemaParser schemaParser = new SchemaParser();
		ClassPathResource schema = new ClassPathResource("schema.graphql");
		//ClassPathResource schema = new ClassPathResource("schema.graphql");
		//TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(String.valueOf(schema.getInputStream()));
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);
		RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
				.type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("getBook", bookService.getBook()))
				.type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("getBooks", bookService.getBooks()))
				.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("createBook", bookService.createBook()))
				.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("deleteAuthor", authorService.deleteAuthor()))
				.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("updateAuthor", authorService.updateAuthor()))
				.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("updateBook", bookService.updateBook()))
				.type(TypeRuntimeWiring.newTypeWiring("Book").dataFetcher("author", authorService.authDataFetcher()))

				.build();
				//.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("createBook", bookService.createBook()))
				//.type(TypeRuntimeWiring.newTypeWiring("Book").dataFetcher("author", authorService.authorDataFetcher()))


		SchemaGenerator generator = new SchemaGenerator();
		GraphQLSchema graphQLSchema = generator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
		return GraphQL.newGraphQL(graphQLSchema).build();
	}

}
