import AuthorInfo from "../models/AuthorInfo";

class AuthorService {
  
  getAllAuthors() : Promise<AuthorInfo[]> {
    const halson = require("halson");

    return fetch('/api/v1/author?projection=authorWithId')
        .then(response => response.json())
        .then(data => halson(data).getEmbeds("authors"))
        .then(authors => authors.map((t: any) => {
            const h = halson(t);
            return {...(h as AuthorInfo), selfHref: h.getLink('self').href}
        }));
  }
}

export default new AuthorService();
