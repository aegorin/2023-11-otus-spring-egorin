import './BooksTable.css'
import { useEffect, useState } from 'react';
import { Link } from "react-router-dom"
import BookInfo from "./BookInformationComponent";
import BookService from "../../api/BookService";
import BookInformation from "../../models/BookInformation";

export default function BooksTable() {
    const [bookInfos, setBookInfos] = useState<BookInformation[]>();

    useEffect(() => {
        BookService.getAllBooks()
        .then(books => setBookInfos(books))
    }, []);

    return (
        <>
        { bookInfos ? (
          <>
            <Link to="/book/add" title="Добавить новую книгу">
              <button className="button-link gray-btn">Добавить</button>
            </Link>
            <table className="books-table">
              <thead>
                <tr>
                  <th style={{width: "0"}}></th>
                  <th style={{width: "50%"}}>Наименование</th>
                  <th>Автор</th>
                  <th>Жанр</th>
                </tr>
              </thead>
              <tbody>
                {bookInfos.map((b) => (
                  <BookInfo key={b.id} {...b}></BookInfo>
                ))}
            </tbody>
            </table>
          </>
        ) : (
          <p>Загрузка списка книг...</p>
        )}
        </>
    );
}
