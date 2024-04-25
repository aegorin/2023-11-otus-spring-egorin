export default interface BookUpdate {
    id: number,
    title: string,
    selfHref: string,
    authorHref: string,
    genreHref: string,
    author?: any,
    genre?: any
}
