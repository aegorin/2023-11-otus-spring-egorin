import AuthorInfo from "./AuthorInfo"
import GenreInfo from "./GenreInfo"

export default interface BookInformation {
    id: number,
    title: string,
    author: AuthorInfo,
    genre: GenreInfo
}
