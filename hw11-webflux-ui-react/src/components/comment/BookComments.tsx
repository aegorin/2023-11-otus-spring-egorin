import { useEffect, useState, useId } from "react";
import CommentBook from "../../models/Comment";
import CommentService from "../../api/CommentService";
import CommentComponent from "./CommentComponent";
import { useParams } from "react-router-dom";
import BookUpdate from "../../models/BookUpdate";
import BookService from "../../api/BookService";

export default function BookComments() {
    const idNewTextComment = useId();
    const formId = useId();
    const {bookId} = useParams();
    const [comments, setComments] = useState<CommentBook[]>();
    const [book, setBook] = useState<BookUpdate>({} as BookUpdate);
    const [textNewComment, setTextNewComment] = useState<string>('');


    useEffect(() => {
        if (!bookId) {
            return;
        }
        CommentService.getCommentsFormBook(bookId)
          .then(c => setComments(c))
        BookService.getBookById(bookId)
          .then(b => setBook(b));
    }, [])
    
    const removeComment = (commentId: number) => {
        CommentService.deleteCommentById(commentId)
          .then(r => {
            if (r.status == 204) {
                const newComments = comments?.filter(c => c.id != commentId)
                setComments(newComments);
            }
          });
    }

    const addNewComment = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!bookId) {
            return;
        }
        CommentService.addNewComment({id: 0, bookId: Number(bookId), text: textNewComment})
          .then(c => {
            comments?.push(c);
            setTextNewComment('');
        });
    }

    return (
        <>
        <h2>Комментарии к книге &laquo;{book.title}&raquo;</h2>
        { comments && comments.length > 0 ? (
            <div className="flex-one-column">
                {comments.map(c => <CommentComponent key={c.id} {...{comment: c, handleDelete: removeComment}}></CommentComponent>)}
            </div>
        ) : (
            <div>Комментарии к книге не найдены</div>
        )}

        <div style={{marginTop: '1.4em'}}>
        <form onSubmit={addNewComment} id={formId}>
          <label htmlFor={idNewTextComment}>
            Новый комментарий к книге:
          </label>
          <div style={{marginTop: ".2em"}}>
            <textarea id={idNewTextComment} 
              value={textNewComment} 
              onChange={e => setTextNewComment(e.target.value)}
              disabled={!bookId}
              rows={4} cols={60}
              className="book-comment"/>
          </div>
        </form>
        <div className="row-buttons" style={{marginTop: "0.2em"}}>
          <button type="submit" 
          disabled={textNewComment.length < 1} 
          form={formId}
          className="button-link green-btn">Добавить</button>
          <button onClick={() => setTextNewComment('')} 
          disabled={textNewComment.length < 1}
          className="button-link gray-btn">Отмена</button>
        </div>
        </div>
        </>
    );
}
