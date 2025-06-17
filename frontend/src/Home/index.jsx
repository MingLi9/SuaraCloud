import { useState, useEffect, useCallback } from 'react';
import gatewayUrl from '../config';
import Sidebar from '../components/Sidebar';
import MainContent from '../components/MainContent';
import Player from '../components/Player';

function App({ logout }) {
    const [songs, setSongs] = useState([]);
    const [currentSong, setCurrentSong] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [audioElement, setAudioElement] = useState(null);

    const fetchSongs = useCallback(async () => {
        try {
            const response = await fetch(`${gatewayUrl}/songs/list`);
            if (!response.ok) {
                throw new Error('Failed to fetch songs');
            }
            const data = await response.json();
            setSongs(data);

            // Set the first song as current if available
            if (data.length > 0 && !currentSong) {
                setCurrentSong(data[0]);
            }
        } catch (error) {
            console.error('Error fetching songs:', error);
        }
    }, [currentSong]);

    useEffect(() => {
        // Fetch songs when component mounts
        fetchSongs();

        // Create audio element
        const audio = new Audio();
        setAudioElement(audio);

        // Cleanup on unmount
        return () => {
            if (audio) {
                audio.pause();
                audio.src = '';
            }
        };
    }, [fetchSongs]);

    const playSong = async (song) => {
        try {
            if (audioElement) {
                // Pause current audio if playing
                audioElement.pause();

                // Set the new song as current
                setCurrentSong(song);

                // Set the audio source to the song URL
                audioElement.src = `${gatewayUrl}/songs/${song.id}`;

                // Play the audio
                audioElement
                    .play()
                    .then(() => {
                        setIsPlaying(true);
                    })
                    .catch((err) => {
                        console.error('Error playing audio:', err);
                        setIsPlaying(false);
                    });
            }
        } catch (error) {
            console.error('Error playing song:', error);
        }
    };

    return (
        <div className='spotify-app'>
            <div className='spotify-main'>
                <Sidebar />
                <MainContent
                    songs={songs}
                    playSong={playSong}
                    currentSong={currentSong}
                    logout={logout}
                />
            </div>
            <Player
                currentSong={currentSong}
                isPlaying={isPlaying}
                setIsPlaying={setIsPlaying}
                audioElement={audioElement}
            />
        </div>
    );
}

export default App;
