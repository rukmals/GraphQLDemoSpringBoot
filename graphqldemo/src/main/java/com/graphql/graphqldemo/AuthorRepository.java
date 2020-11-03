package com.graphql.graphqldemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class AuthorRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<UUID> createAuthor(Author author){
        UUID authorId = UUID.randomUUID();
        author.setId(authorId);
        return databaseClient.insert().into(Author.class).using(author).then().thenReturn(authorId);


    }

    public Mono<Author> getAuthor(UUID bookId){
        return  databaseClient.select().from(Author.class).matching(Criteria.where("book_id").is(bookId)).fetch().one();
    }

    public Mono<Boolean> deleteAuthor(UUID authorId){
        return databaseClient.delete().from(Author.class).matching(Criteria.where("id").is(authorId)).then().thenReturn(true);
    }
    public Mono<String> updateAuthpr(UUID authorId,Author author){
        return databaseClient.update().table("authors").using(Update.update("name",author.getName())).matching(Criteria.where("id").is(authorId))
                .then().thenReturn("Success");
    }

}
