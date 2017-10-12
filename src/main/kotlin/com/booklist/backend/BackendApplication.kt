package com.booklist.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class BackendApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(BackendApplication::class.java, *args)
    }

    @Bean
    fun startup(userService: UserService) = CommandLineRunner {
        userService.register("erik", "erik@gmail.com")
    }


}

@RestController
@RequestMapping("/books")
class BookController(val bookRepository: BookRepository) {

    @GetMapping
    fun list() = bookRepository.findAll()

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Long): Book = bookRepository.findById(id).orElseThrow { NotFoundException() }

    @PostMapping
    fun save(book: Book) = bookRepository.save(book)

}


@Service
class UserService(val userRepository: UserRepository) {

    fun register(name: String, emailAddress: String): User {
        return User(name = name, emailAddress = emailAddress, booklists = emptyList())
    }
}

@Service
class ListService(val booklistRepository: BooklistRepository) {

    fun createList(name: String, user: User) {

    }

    fun addBook(user: User, book: Book) {

    }

    fun removeBook(user: User, book: Book) {

    }
}


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Object not found")
class NotFoundException : RuntimeException()

@Repository
interface BookRepository : JpaRepository<Book, Long>

@Repository
interface UserRepository : JpaRepository<User, Long>

@Repository
interface BooklistRepository : JpaRepository<Booklist, Long>


@Entity
class Book(@Id @GeneratedValue val id: Long = -1, val isbn: String, val title: String, val author: String)

@Entity
class User(@Id @GeneratedValue val id: Long = -1, val name: String, val emailAddress: String, val booklists: List<Booklist>)

@Entity
class Booklist(@Id @GeneratedValue val id: Long = -1, val name: String = "Booklist", val books: List<Book>)