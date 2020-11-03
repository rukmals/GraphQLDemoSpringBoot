package com.graphql.graphqldemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class BookRepository {


    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Book> getBook(UUID id){
        return databaseClient.select()
                .from(Book.class)
                .matching(Criteria.where("id").is(id))
                .fetch()
                .one();
    }

    public Flux<Book> getBooks(){
        return databaseClient.select().from(Book.class).fetch().all();
    }

    public Mono<UUID> createBook(Book book){
        UUID bookId = UUID.randomUUID();
        book.setId(bookId);

        return databaseClient.insert()
                .into(Book.class)
                .using(book)
                .then()
                .thenReturn(bookId);

    }

    public Mono<Boolean> deleteBook(UUID id){
        return databaseClient.delete()
                .from(Book.class).matching(Criteria.where("id").is(id)).then().thenReturn(true);
        //return true;
    }

    public Mono<String> updateBook(UUID ID,Book book){
        return databaseClient.update()
            .table("books")
            //.using(Update.update(Book.class,book))
            .using(Update.update("name",book.getName()))
            .matching(Criteria.where("id").is(ID))
            .then().thenReturn("Success");
    }


//    Mono<Void> update = databaseClient.update()
//            .table("books")
//            .using(Update.update("name", "Book_E"))
//            .matching(Criteria.where("name").is("Book_D"))
//            .then();
//




}
