import React from 'react';

const SongRow = ({ index, title, artist, duration, isActive, onClick }) => {
    return (
        <div
            className={`song-row ${isActive ? 'active' : ''}`}
            onClick={onClick}
        >
            <div className='song-number'>{index}</div>
            <div className='song-cover'>{title[0]}</div>
            <div className='song-info'>
                <p className='song-title'>{title}</p>
                <p className='song-artist'>{artist}</p>
            </div>
            <div className='song-duration'>{duration}</div>
        </div>
    );
};

export default SongRow;
