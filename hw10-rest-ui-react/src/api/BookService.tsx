import BookInformation from "../models/BookInformation";
import BookUpdate from "../models/BookUpdate";

class BookService {
  
  getAllBooks() : Promise<BookInformation[]> {
    return fetch('/api/v1/book')
      .then(response => response.json())
      .then(item => item.map((b : any) => b as BookInformation));
  }

  getBookById(bookId: string) : Promise<BookUpdate> {
    return fetch(`/api/v1/book/${bookId}`)
    .then(response => response.json())
    .then(b => b as BookUpdate);
  }

  addNewBook(book: BookUpdate) : Promise<BookUpdate> {
    return fetch(`/api/v1/book`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(book)
    })
      .then(response => response.json())
      .then(item => item as BookUpdate);
  }

  deleteBookById(bookId: number) : Promise<Response> {
    return fetch(`/api/v1/book/${bookId}`, {method: 'DELETE'});
  }

  updateBook(book: BookUpdate) : Promise<Response> {
    return fetch(`/api/v1/book/${book.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(book)
    });
  }
}

export default new BookService();
