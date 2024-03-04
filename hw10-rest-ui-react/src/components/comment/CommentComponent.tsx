import { useState } from "react";
import CommentBook from "../../models/Comment";
import CommentService from "../../api/CommentService";

export default function CommentComponent(props: {comment: CommentBook, handleDelete : (id:number) => void}) {

    const {comment, handleDelete} = props;
    const [editMode, setEditMode] = useState<boolean>(false);
    const [textComment, setTextComment] = useState<string>(comment.text);

    const updateTextComment = () => {
        comment.text = textComment;
        CommentService.updateComment(comment).then(r => setEditMode(!editMode));
    }
      
    return (
        <>
        {editMode ? 
        <div>
            <div style={{marginBottom: '0.5em'}}>
            <textarea
              value={textComment}
              onChange={e => setTextComment(e.target.value)}
              rows={3} cols={60}
              className="book-comment"/>
            </div>
            <div className="row-buttons">
            <button onClick={() => updateTextComment()} disabled={textComment.length < 1}
            className="button-link green-btn">Сохранить</button>

            <button onClick={() => {setTextComment(comment.text); setEditMode(!editMode)}}
            className="button-link gray-btn">Отмена</button>
            </div>
        </div>
        : null}

        {!editMode ? 
          <div>
            <div style={{paddingTop: ".7em"}}>
              {comment.text}
            </div>
            <div className="row-buttons">
              <button onClick={() => setEditMode(!editMode)} className="button-link gray-btn">Редактировать</button>
              <button onClick={() => handleDelete(comment.id)} className="button-link red-btn">Удалить</button>
            </div>
          </div>
        : null}
        </>
    );
}