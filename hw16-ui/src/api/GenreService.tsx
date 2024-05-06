import GenreInfo from "../models/GenreInfo";

class GenreService {
  
  getAllGenres() : Promise<GenreInfo[]> {
    const halson = require("halson");

    return fetch('/api/v1/genre?projection=genreWithId')
        .then(response => response.json())
        .then(data => halson(data).getEmbeds("genres"))
        .then(genres => genres.map((t: any) => {
            const h = halson(t);
            return {...(h as GenreInfo), selfHref: h.getLink('self').href}
        }));
  }
}

export default new GenreService();
