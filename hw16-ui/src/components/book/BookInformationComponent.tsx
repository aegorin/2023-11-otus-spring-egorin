import BookInformation from '../../models/BookInformation';
import { Link } from "react-router-dom"

export default function BookInfoTableRow(book: BookInformation) {
    return (
      <tr>
        <td className="book-controls">
          <div>
            <Link to={"/book/" + book.id + "/comment"} title="Комментарии">
              <img className="comment" title="Комментарии" />
            </Link>
          </div>
          <div>
            <Link to={"/book/" + book.id} title="Редактировать">
              <img className="edit" title="Редактировать" />
            </Link>
          </div>
        </td>
        <td>
          {book.title}
        </td>
        <td>
          {book.author.fullName}
        </td>
        <td>
          {book.genre.name}
        </td>
      </tr>
    );
}
