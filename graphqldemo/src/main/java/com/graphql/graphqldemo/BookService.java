package com.graphql.graphqldemo;


import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public DataFetcher<CompletableFuture<Book>> getBook(){
        return env-> {
            String bookId = env.getArgument("id");
            return bookRepository.getBook(UUID.fromString(bookId)).toFuture();
        };
    }


    public DataFetcher<CompletableFuture<List<Book>>> getBooks(){
        return env-> bookRepository.getBooks().collectList().toFuture();

    }

    public DataFetcher<CompletableFuture<String>> createBook(){
        return env->{
            String bookName = env.getArgument("bookName");
            String authorName = env.getArgument("authorName");

            int page = env.getArgument("pages");
            int age = env.getArgument("age");

            Category category = Category.valueOf(env.getArgument("category"));

            Book  book = new Book();
            book.setName(bookName);
            book.setPage(page);
            book.setCategory(category);

            return bookRepository.createBook(book).flatMap(bookId->
                authorService.createAuthor(authorName,age,bookId).map(authorId->bookId.toString())
            ).toFuture();

            //return  bookRepository.createBook(new Book(name,page)).map(Objects::toString).toFuture();


            // return null;
        };
    }
    public DataFetcher<CompletableFuture<Boolean>> deleteBook(){
        return env->{
            String id = env.getArgument("id");
            return bookRepository.deleteBook(UUID.fromString(id)).toFuture();
        };
    }
    public DataFetcher<CompletableFuture<String>> updateBook(){
        return env->{
            String id = env.getArgument("id");
            String bookName = env.getArgument("bookName");
            int pages = env.getArgument("pages");
            Category category = Category.valueOf(env.getArgument("category"));
            Book book = new Book();
            book.setName(bookName);
            book.setPage(pages);
            book.setCategory(category);


            //System.out.println(bookName);
            return  bookRepository.updateBook(UUID.fromString(id),book).toFuture();

        };

    }


}
