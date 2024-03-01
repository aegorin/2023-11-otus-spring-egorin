import './App.css';
import { Route, Routes, NavLink } from "react-router-dom"
import BooksTable from './components/book/BooksTable';
import AuthorsTable from './components/author/AuthorsTable';
import GenresTable from './components/genre/GenresTable';
import BookComments from './components/comment/BookComments';
import BookEdit from './components/book/BookEdit';

function App() {
  return (
    <div className="App">
      <nav className="navbar">
        <ul className="navbar-links">
          <li><NavLink to="/">Книги</NavLink></li>
          <li><NavLink to="/authors">Авторы</NavLink></li>
          <li><NavLink to="/genres">Жанры</NavLink></li>
        </ul>
      </nav>
      <Routes>
        <Route path="/" element={<BooksTable/>} />
        <Route path="/authors" element={<AuthorsTable/>} />
        <Route path="/genres" element={<GenresTable/>} />
        <Route path="/book">
          <Route path="add" element={<BookEdit/>} />
          <Route path=":bookId" element={<BookEdit/>} />
          <Route path=":bookId/comment" element={<BookComments/>} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
