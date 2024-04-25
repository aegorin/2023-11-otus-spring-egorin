import { useEffect, useState } from 'react';
import GenreInfo from '../../models/GenreInfo';
import GenreService from '../../api/GenreService';

export default function GenresTable() {
    const [genres, setGenres] = useState<GenreInfo[]>();

    useEffect(() => {
        GenreService.getAllGenres()
        .then(a => setGenres(a))
    }, []);

    return (
        <>
        {
            genres ? (
                <div className="flex-one-column">
                    {genres.map((a, idx) => 
                    <div key={idx}>{a.name}</div>)}
                </div>
            ) : (
                <p>Загрузка списка жанров...</p>
            )
        }
        </>
    );
}
