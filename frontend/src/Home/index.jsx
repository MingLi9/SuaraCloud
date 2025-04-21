import React, { useState } from 'react';
import Sidebar from '../components/Sidebar';
import MainContent from '../components/MainContent';
import Player from '../components/Player';

function Home() {
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
}

export default Home;
