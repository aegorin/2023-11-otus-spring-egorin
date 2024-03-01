import CommentBook from "../models/Comment";

class CommentService {
  
  getCommentsFormBook(bookId: string) : Promise<CommentBook[]> {
    return fetch(`/api/v1/book/${bookId}/comment`)
      .then(response => response.json())
      .then(item => item.map((b : any) => b as CommentBook));
  }

  deleteCommentById(commentId: number) : Promise<Response> {
    return fetch(`/api/v1/comment/${commentId}`, {method: 'DELETE'});
  }

  addNewComment(comment: CommentBook) : Promise<CommentBook> {
    return fetch(`/api/v1/comment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(comment)
    })
      .then(response => response.json())
      .then(item => item as CommentBook);
  }

  updateComment(comment: CommentBook) : Promise<Response> {
    return fetch(`/api/v1/comment/${comment.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(comment)
    });
  }
}

export default new CommentService();
