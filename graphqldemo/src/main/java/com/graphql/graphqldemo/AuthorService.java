package com.graphql.graphqldemo;


import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Mono<String> createAuthor(String name, int age, UUID bookId){
        Author author = new Author();
        author.setName(name);
        author.setAge(age);
        author.setBookId(bookId);

        return authorRepository.createAuthor(author).map(Objects::toString);

    }

    public DataFetcher<CompletableFuture<Author>> authDataFetcher(){
        return env->{
            Book book = env.getSource();
            return authorRepository.getAuthor(book.getId()).toFuture();
        };
    }

    public DataFetcher<CompletableFuture<Boolean>> deleteAuthor(){
        return env->{
            String authorId = env.getArgument("id");
            return authorRepository.deleteAuthor(UUID.fromString(authorId)).toFuture();
        };

    }

    public DataFetcher<CompletableFuture<String>> updateAuthor(){
        return env->{
            String authorId = env.getArgument("id");
            String name = env.getArgument("name");
            int age = env.getArgument("age");

            Author author = new Author();
            author.setName(name);
            author.setAge(age);

            return authorRepository.updateAuthpr(UUID.fromString(authorId),author).toFuture();

        };
    }
}
