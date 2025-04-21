import React, { useState } from 'react';
import Sidebar from './Sidebar';
import MainContent from './MainContent';
import Player from './Player';

const SpotifyClone = () => {
    const [currentSong, setCurrentSong] = useState({
        title: 'Blinding Lights',
        artist: 'The Weeknd',
        album: 'After Hours',
        duration: '3:20',
    });

    return (
        <div className='spotify-app'>
            <div className='spotify-main'>
                <Sidebar />
                <MainContent setCurrentSong={setCurrentSong} />
            </div>
            <Player currentSong={currentSong} />
        </div>
    );
};

export default SpotifyClone;
