import React from 'react';
import Header from './Header';
import AlbumCard from './AlbumCard';
import SongRow from './SongRow';

const MainContent = ({ setCurrentSong, logout }) => {
    return (
        <div className='main-content'>
            <Header logout={logout} />

            {/* Recently Played */}
            <h2 className='section-title'>Recently Played</h2>
            <div className='album-grid'>
                {Array.from({ length: 5 }).map((_, i) => (
                    <AlbumCard
                        key={i}
                        title={`Album ${i + 1}`}
                        subtitle={`Artist ${i + 1}`}
                        index={i + 1}
                    />
                ))}
            </div>

            {/* Made For You */}
            <h2 className='section-title'>Made For You</h2>
            <div className='album-grid'>
                {Array.from({ length: 5 }).map((_, i) => (
                    <AlbumCard
                        key={i}
                        title={`Daily Mix ${i + 1}`}
                        subtitle='Based on your listening'
                        index={i + 1}
                    />
                ))}
            </div>

            {/* Top Songs */}
            <h2 className='section-title'>Top Songs</h2>
            <div className='song-list'>
                {Array.from({ length: 10 }).map((_, i) => (
                    <SongRow
                        key={i}
                        index={i + 1}
                        title={`Song ${i + 1}`}
                        artist={`Artist ${i + 1}`}
                        duration='3:20'
                        isActive={i === 0}
                        onClick={() =>
                            setCurrentSong({
                                title: `Song ${i + 1}`,
                                artist: `Artist ${i + 1}`,
                                album: `Album ${i + 1}`,
                                duration: '3:20',
                            })
                        }
                    />
                ))}
            </div>
        </div>
    );
};

export default MainContent;
