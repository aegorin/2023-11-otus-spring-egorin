import BookInformation from "../models/BookInformation";
import BookUpdate from "../models/BookUpdate";

class BookService {
  
  getAllBooks() : Promise<BookInformation[]> {
    const halson = require("halson");
    return fetch('/api/v1/book?projection=withAuthorGenre')
      .then(response => response.json())
      .then(data => halson(data).getEmbeds("books"))
      .then(books => books.map((t: any) => {
        return t as BookInformation;
    }));
  }

  getBookById(bookId: string) : Promise<BookUpdate> {
    const halson = require("halson");
    return fetch(`/api/v1/book/${bookId}?projection=withAuthorGenre`)
    .then(response => response.json())
    .then(data => {
      const h = halson(data);
      return {...(h as BookUpdate), 
        id: Number(bookId),
        selfHref: h.getLink('self').href, 
        authorHref: halson(h.author).getLink('self').href.replace('{?projection}', ''),
        genreHref: halson(h.genre).getLink('self').href.replace('{?projection}', ''),
        author: h._links.author,
        genre: h._links.genre
      }
    })
  }

  addNewBook(book: BookUpdate) : Promise<BookUpdate> {
    const bookData = {title: book.title, author: book.authorHref, genre: book.genreHref};
    return fetch(`/api/v1/book`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(bookData)
    })
      .then(response => response.json())
      .then(item => item as BookUpdate);
  }

  deleteBookById(bookId: number) : Promise<Response> {
    return fetch(`/api/v1/book/${bookId}`, {method: 'DELETE'});
  }

  updateBook(book: BookUpdate) : Promise<Response> {
    return fetch(`/api/v1/book/${book.id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify({title: book.title})
    }).then(r => fetch(book.author.href.replace('{?projection}', ''), {
      method: 'PUT',
      headers: {
        'Content-Type': 'text/uri-list;charset=utf-8'
      },
      body: book.authorHref
    })).then(r => fetch(book.genre.href.replace('{?projection}', ''), {
      method: 'PUT',
      headers: {
        'Content-Type': 'text/uri-list;charset=utf-8'
      },
      body: book.genreHref
    }));
  }
}

export default new BookService();
