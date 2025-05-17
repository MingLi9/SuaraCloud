import React from 'react';
import { PlayIcon } from './Icons';

const AlbumCard = ({ title, subtitle, index, song, playSong }) => {
    return (
        <div className='album-card'>
            <div className='album-cover-container'>
                <div className='album-cover'>{song}</div>
                <button className='play-button' onClick={() => playSong(song)}>
                    <PlayIcon className='icon-sm' />
                </button>
            </div>
            <h3 className='album-title'>{title}</h3>
            <p className='album-artist'>{subtitle}</p>
        </div>
    );
};

export default AlbumCard;
