import AuthorInfo from "../models/AuthorInfo";

class AuthorService {
  
  getAllAuthors() : Promise<AuthorInfo[]> {
    return fetch('/api/v1/author')
      .then(response => response.json())
      .then(item => item.map((b : any) => b as AuthorInfo));
  }
}

export default new AuthorService();
