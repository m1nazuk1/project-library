package com.example.springBoot.controllers;

import com.example.springBoot.models.Book;
import com.example.springBoot.models.Person;
import com.example.springBoot.services.BookService;
import com.example.springBoot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
@RequestMapping("/library/books")
public class BooksController {
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BooksController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping
    public String showBooks(@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
                            @RequestParam(value = "books_per_page", required = false, defaultValue = "-1") int booksPerPage,
                            @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") boolean sortByYear,
                            Model model){
        List<Book> books;
        if (page != -1 && booksPerPage != -1){
            books = sortByYear
                    ? bookService.findAndPageAndSortByYear(page, booksPerPage)
                    : bookService.findAndPage(page, booksPerPage);
        }else if (page == 1 && booksPerPage == -1 && sortByYear){
            books = bookService.findAndSortByYear();
        }else {
            books = bookService.findAll();
        }
        model.addAttribute("books", books);
        return "books/show";
    }

    @GetMapping("/{irinaLoh}")
    public String test(@PathVariable("irinaLoh") String a){
        //дофига кода
        String pashkaLoh = "json";
        return pashkaLoh;
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        Book book = bookService.findOneById(id);
        model.addAttribute("book", book);
        if (book.getReader() != null){
            model.addAttribute("reader", book.getReader());
        }else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/profile";
    }

    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "books/new";
        bookService.save(book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookService.findOneById(id));
        return "books/edit";
    }


    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int id){
        if (bindingResult.hasErrors()) return "books/edit";
        bookService.update(id, book);
        return "redirect:/library/books";
    }


    @PatchMapping("/{id}/person")
    public String addBookToPerson(@ModelAttribute("person") Person person,
                                  @PathVariable("id") int id){
        bookService.addBookToPerson(person, id);
        return "redirect:/library/books/{id}";
    }


    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id){
        bookService.freeBook(id);
        return "redirect:/library/books/{id}";
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam(required = false, defaultValue = "") String strartString,
                             Model model){
        model.addAttribute("startString", strartString);
        if (!strartString.isEmpty()){
            model.addAttribute("books", bookService.searchBooks(strartString));
        }
        return "books/search";
    }


    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/library/books";
    }
}
