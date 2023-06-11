package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expectedBook = new Book("1231231231", "Title", "Author", 9.90);
		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).
							isEqualTo(expectedBook.isbn());
				});
	}

	@Test
	void whenGetRequestWithIdThenBookReturned() {
		String isbn = new String("1231231231");
		var expectedBook = new Book(isbn, "Title", "Author", 9.90);
		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).
							isEqualTo(expectedBook.isbn());
				});

		webTestClient.get()
				.uri("/books/" + isbn)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).
							isEqualTo(expectedBook.isbn());
				});
	}

	@Test
	void whenPutRequestThenBookUpdated() {
		String isbn = new String("1231231231");
		var expectedBook = new Book(isbn, "Title", "Author", 9.90);

		Book createdBook = webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
				})
				.returnResult().getResponseBody();

		var updatedBook = new Book(isbn, createdBook.title(), createdBook.author(), 10.9);

		webTestClient
				.put()
				.uri("/books/"+isbn)
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.price()).
							isEqualTo(updatedBook.price());
				});

	}

	@Test
	void whenDeleteRequestThenBookDeleted() {

		String isbn = new String("1231231231");
		var expectedBook = new Book(isbn, "Title", "Author", 9.90);
		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
				});

		webTestClient
				.delete()
				.uri("/books/"+isbn)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/books/" + isbn)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class).value(errorMessage -> {
					assertThat(errorMessage)
							.isEqualTo("The book with ISBN " + isbn + " was not found.");
				});
	}
}