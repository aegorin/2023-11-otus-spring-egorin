import CommentBook from "../models/Comment";

class CommentService {
  
  getCommentsFormBook(bookId: string) : Promise<CommentBook[]> {
    const halson = require("halson");
    return fetch(`/api/v1/comment/search/book?bookId=${bookId}`)
      .then(response => response.json())
      .then(data => halson(data).getEmbeds("comments"))
      .then(comments => comments.map((t: any) => {
        const h = halson(t);
        return {text: h.text, selfHref: h.getLink('self').href, book: h.getLink('self').href};
    }));
  }

  deleteComment(comment: CommentBook) : Promise<Response> {
    let url = comment.selfHref;
    return fetch(url, {method: 'DELETE'});
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
    return fetch(comment.selfHref, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(comment)
    });
  }
}

export default new CommentService();
