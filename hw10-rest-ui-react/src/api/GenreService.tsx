import GenreInfo from "../models/GenreInfo";

class GenreService {
  
  getAllGenres() : Promise<GenreInfo[]> {
    return fetch('/api/v1/genre')
      .then(response => response.json())
      .then(item => item.map((b : any) => b as GenreInfo));
  }
}

export default new GenreService();
