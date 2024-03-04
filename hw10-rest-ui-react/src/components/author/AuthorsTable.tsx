import { useEffect, useState } from 'react';
import AuthorInfo from '../../models/AuthorInfo';
import AuthorService from '../../api/AuthorService';

export default function AuthorsTable() {
    const [authors, setAuthors] = useState<AuthorInfo[]>();

    useEffect(() => {
        AuthorService.getAllAuthors()
        .then(a => setAuthors(a))
    }, []);

    return (
        <>
        {
            authors ? (
                <div className="flex-one-column">
                    {authors.map((a) => 
                    <div key={a.id}>{a.fullName}</div>)}
                </div>
            ) : (
                <p>Загрузка списка авторов...</p>
            )
        }
        </>
    );
}
