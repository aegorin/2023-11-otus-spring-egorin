import { useEffect, useId, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import BookUpdate from "../../models/BookUpdate";
import BookService from "../../api/BookService";
import GenreService from "../../api/GenreService";
import GenreInfo from "../../models/GenreInfo";
import AuthorInfo from "../../models/AuthorInfo";
import AuthorService from "../../api/AuthorService";

export default function BookEdit() {
    
    const {bookId} = useParams();
    const formId = useId();
    const [bookUpdate, setBookUpdate] = useState<BookUpdate>({
        id: 0,
        title: "",
        selfHref: "",
        authorHref: "",
        genreHref: ""
    });
    const [genres, setGenres] = useState<GenreInfo[]>([]);
    const [authors, setAuthors] = useState<AuthorInfo[]>([]);
    
    useEffect(() => {
        GenreService.getAllGenres().then(g => setGenres(g));
        AuthorService.getAllAuthors().then(a => setAuthors(a));

        if (bookId) {
            BookService.getBookById(bookId)
              .then(b => setBookUpdate({...b}));
        }
    }, [bookId])

    const changedValue = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setBookUpdate({...bookUpdate, [name]: value});
    }

    const changedOptionValue = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = event.target;
        setBookUpdate({...bookUpdate, [name]: value});
    }

    const navigate = useNavigate();
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (bookId) {
          BookService.updateBook(bookUpdate)
            .then(() => navigate("/"));
        }
        else {
            BookService.addNewBook(bookUpdate)
              .then(() => navigate("/"));
        }
    }

    const deleteBook = () => {
        BookService.deleteBookById(Number(bookId))
          .then(() => navigate("/"));
    }

    return (
        <div>
            {bookId ? <h3>Форма редактирования книги</h3> : <h3>Добавление новой книги</h3>}
            <form onSubmit={e => handleSubmit(e)} id={formId}>
                <input type="hidden" name="id" value={bookId} />
            </form>

            <table className="books-table">
                <tbody>
                    <tr>
                        <td style={{width: 0}}>Наименование</td>
                        <td>
                            <input id="title" type="text" name="title" 
                            value={bookUpdate.title} 
                            onChange={(e) => changedValue(e)}
                            style={{minWidth: "400px"}}
                            form={formId}/>
                        </td>
                    </tr>
                    <tr>
                        <td>Автор</td>
                        <td>
                        <select id="authorId" name="authorHref" 
                        value={bookUpdate.authorHref} 
                        onChange={(e) => changedOptionValue(e)}
                        form={formId}>
                            {authors.map(a => <option key={'a' + a.id} value={a.selfHref}>{a.fullName}</option>)}
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Жанр</td>
                        <td>
                        <select id="genreId" name="genreHref" 
                        value={bookUpdate.genreHref} 
                        onChange={(e) => changedOptionValue(e)}
                        form={formId}>
                            {genres.map(g => <option key={'g' + g.id} value={g.selfHref}>{g.name}</option>)}
                        </select>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div className="row-buttons">
                <button type="submit" disabled={bookUpdate.title.length < 2} 
                form={formId}
                className="button-link green-btn">Сохранить</button>
                
                <Link to="/">
                  <button className="button-link gray-btn">Отмена</button>
                </Link>
                
                {bookId ? <button onClick={deleteBook} className="button-link red-btn">Удалить книгу</button> : null}
            </div>
        </div>
    );
}
