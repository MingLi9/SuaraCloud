import React from 'react';
import { PlayIcon } from './Icons';

const AlbumCard = ({ title, subtitle, index }) => {
    return (
        <div className='album-card'>
            <div className='album-cover-container'>
                <div className='album-cover'>
                    {title[0]}
                    {index}
                </div>
                <button className='play-button'>
                    <PlayIcon className='icon-sm' />
                </button>
            </div>
            <h3 className='album-title'>{title}</h3>
            <p className='album-artist'>{subtitle}</p>
        </div>
    );
};

export default AlbumCard;
