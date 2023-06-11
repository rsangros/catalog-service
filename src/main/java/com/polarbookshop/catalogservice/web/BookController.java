package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    public BookController (BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get(){
        return this.bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn){
        return this.bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post (@Valid @RequestBody Book book){
        return this.bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable String isbn){
        this.bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    public Book put (@PathVariable String isbn, @Valid @RequestBody Book book){
        return this.bookService.editBookDetails(isbn, book);
    }
}
