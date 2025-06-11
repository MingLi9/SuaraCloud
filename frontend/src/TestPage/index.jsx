import gatewayUrl from '../config';
import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import { headers } from '../config';
import Hls from 'hls.js';

function TestPage() {
    const [data, setData] = useState(null);
    const [protectedResourceData, setProtectedResourceData] = useState(null);
    const [songs, setSongs] = useState([]);
    const [currentSong, setCurrentSong] = useState(null);
    const [currentBitrate, setCurrentBitrate] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const audioRef = useRef(null);
    const hlsRef = useRef(null);

    useEffect(() => {
        // Your existing API calls
        axios
            .post(`${gatewayUrl}/demo/mqtt`, 'test', {
                headers: {
                    'Content-Type': 'application/json',
                },
            })
            .then((response) => {
                setData(response.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });

        // Check protected resource
        axios
            .get(`${gatewayUrl}/api/protected-resource`, {
                headers,
            })
            .then((response) => {
                setProtectedResourceData(response.data);
            })
            .catch((error) => {
                console.error(
                    'Error fetching /protected-resource data:',
                    error
                );
            });

        // Fetch available songs
        fetchSongs();

        // Cleanup HLS on unmount
        return () => {
            if (hlsRef.current) {
                hlsRef.current.destroy();
            }
        };
    }, []);

    const fetchSongs = async () => {
        setIsLoading(true);
        setError(null);
        try {
            // Fetch song metadata from your song-meta-service
            const response = await axios.get(
                `${gatewayUrl}/songmeta/artist/5`,
                {
                    headers,
                }
            );
            setSongs(response.data);
        } catch (error) {
            console.error('Error fetching songs:', error);
            setError('Failed to fetch songs');
        } finally {
            setIsLoading(false);
        }
    };

    const playMp3Song = async (song) => {
        try {
            // Cleanup any existing HLS
            if (hlsRef.current) {
                hlsRef.current.destroy();
                hlsRef.current = null;
            }

            setCurrentSong(song);
            setCurrentBitrate('MP3');
            setError(null);

            // Extract UUID from originalUrl (remove .mp3 extension)
            const uuid = song.originalUrl.replace('.mp3', '');
            const mp3Url = `${gatewayUrl}/songs/${uuid}.mp3`;

            // Create authenticated blob URL for MP3
            const response = await axios.get(mp3Url, {
                headers,
                responseType: 'blob',
            });

            const audioBlob = new Blob([response.data], { type: 'audio/mpeg' });
            const audioUrl = URL.createObjectURL(audioBlob);

            if (audioRef.current) {
                audioRef.current.src = audioUrl;
            }
        } catch (error) {
            console.error('Error playing MP3:', error);
            setError('Failed to play MP3 file');
        }
    };

    const playHlsBitrate = async (song, bitrate) => {
        console.log('Song details:', song);
        console.log('Status:', song.processingStatus);
        console.log('HLS Master URL:', song.hlsMasterPlaylistUrl);

        if (
            song.processingStatus !== 'COMPLETED' ||
            !song.hlsMasterPlaylistUrl
        ) {
            setError(`HLS processing not completed yet for ${bitrate}`);
            return;
        }

        try {
            setCurrentSong(song);
            setCurrentBitrate(bitrate);
            setError(null);

            // Extract UUID from originalUrl (remove .mp3 extension)
            const uuid = song.originalUrl.replace('.mp3', '');

            // Let's check the actual structure of hlsMasterPlaylistUrl
            console.log('Original HLS URL:', song.hlsMasterPlaylistUrl);

            // The hlsMasterPlaylistUrl should be something like "hls/uuid/master.m3u8"
            // Let's parse it to get the correct structure
            let hlsUuid;
            if (song.hlsMasterPlaylistUrl) {
                const parts = song.hlsMasterPlaylistUrl.split('/');
                if (parts.length >= 2) {
                    hlsUuid = parts[1]; // This should be the UUID
                    console.log('Extracted HLS UUID:', hlsUuid);
                }
            }

            // Use the extracted UUID if available, otherwise fall back to the original UUID
            const finalUuid = hlsUuid || uuid;
            console.log('Final UUID for HLS:', finalUuid);

            const hlsVariantUrl = `${gatewayUrl}/songs/hls/${finalUuid}/${bitrate}/playlist.m3u8`;
            console.log('HLS Variant URL:', hlsVariantUrl);

            if (Hls.isSupported()) {
                // Cleanup existing HLS instance
                if (hlsRef.current) {
                    hlsRef.current.destroy();
                }

                hlsRef.current = new Hls({
                    xhrSetup: (xhr, url) => {
                        // Add authentication headers to HLS requests
                        Object.keys(headers).forEach((key) => {
                            xhr.setRequestHeader(key, headers[key]);
                        });
                    },
                });

                hlsRef.current.loadSource(hlsVariantUrl);
                hlsRef.current.attachMedia(audioRef.current);

                hlsRef.current.on(Hls.Events.MANIFEST_PARSED, () => {
                    console.log(
                        `HLS ${bitrate} manifest parsed, ready to play`
                    );
                });

                hlsRef.current.on(Hls.Events.ERROR, (event, data) => {
                    console.error(`HLS ${bitrate} error:`, data);
                    if (data.fatal) {
                        console.log(
                            `HLS ${bitrate} failed, falling back to MP3`
                        );
                        setError(`HLS ${bitrate} failed, falling back to MP3`);
                        playMp3Song(song);
                    }
                });
            } else if (
                audioRef.current.canPlayType('application/vnd.apple.mpegurl')
            ) {
                // Native HLS support (Safari)
                try {
                    const response = await axios.get(hlsVariantUrl, {
                        headers,
                        responseType: 'text',
                    });
                    const playlistBlob = new Blob([response.data], {
                        type: 'application/vnd.apple.mpegurl',
                    });
                    const playlistUrl = URL.createObjectURL(playlistBlob);
                    audioRef.current.src = playlistUrl;
                } catch (error) {
                    console.log(
                        `Native HLS ${bitrate} failed, falling back to MP3`
                    );
                    playMp3Song(song);
                }
            } else {
                // Fallback to MP3
                console.log(`HLS not supported for ${bitrate}, using MP3`);
                playMp3Song(song);
            }
        } catch (error) {
            console.error(`Error playing HLS ${bitrate}:`, error);
            setError(`Failed to play HLS ${bitrate} stream`);
            // Fallback to MP3
            playMp3Song(song);
        }
    };

    const formatDuration = (seconds) => {
        if (!seconds) return 'Unknown';
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = Math.floor(seconds % 60);
        return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
    };

    const getStatusColor = (status) => {
        switch (status) {
            case 'COMPLETED':
                return '#28a745';
            case 'PROCESSING':
                return '#ffc107';
            case 'PENDING':
                return '#6c757d';
            case 'FAILED':
                return '#dc3545';
            default:
                return '#6c757d';
        }
    };

    const getBitrateButtonStyle = (bitrate, isActive) => ({
        padding: '6px 12px',
        backgroundColor: isActive ? '#0056b3' : '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        fontSize: '12px',
        fontWeight: isActive ? 'bold' : 'normal',
        transform: isActive ? 'scale(1.05)' : 'scale(1)',
        transition: 'all 0.2s ease',
    });

    const getMp3ButtonStyle = (isActive) => ({
        padding: '8px 16px',
        backgroundColor: isActive ? '#1e7e34' : '#28a745',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        fontWeight: isActive ? 'bold' : 'normal',
        transform: isActive ? 'scale(1.05)' : 'scale(1)',
        transition: 'all 0.2s ease',
    });

    return (
        <div
            className='App'
            style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}
        >
            <div data-testid='gateway_url'>gateway_url = {gatewayUrl}</div>
            <div>data = {data}</div>

            {/* Error Display */}
            {error && (
                <div
                    style={{
                        backgroundColor: '#f8d7da',
                        color: '#721c24',
                        padding: '10px',
                        borderRadius: '5px',
                        margin: '10px 0',
                    }}
                >
                    {error}
                </div>
            )}

            {/* Song List */}
            <div>
                <div
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '10px',
                    }}
                >
                    <h3>Available Songs:</h3>
                    <button
                        onClick={fetchSongs}
                        disabled={isLoading}
                        style={{
                            padding: '5px 10px',
                            backgroundColor: '#007bff',
                            color: 'white',
                            border: 'none',
                            borderRadius: '3px',
                            cursor: isLoading ? 'not-allowed' : 'pointer',
                        }}
                    >
                        {isLoading ? 'Loading...' : 'Refresh'}
                    </button>
                </div>

                {isLoading ? (
                    <div>Loading songs...</div>
                ) : songs.length === 0 ? (
                    <div>No songs available</div>
                ) : (
                    songs.map((song) => {
                        const isCurrentSong = currentSong?.id === song.id;
                        return (
                            <div
                                key={song.id}
                                style={{
                                    margin: '10px 0',
                                    padding: '15px',
                                    border: '1px solid #ddd',
                                    borderRadius: '8px',
                                    backgroundColor: isCurrentSong
                                        ? '#e3f2fd'
                                        : '#f9f9f9',
                                }}
                            >
                                <h4 style={{ margin: '0 0 10px 0' }}>
                                    {song.title}
                                </h4>
                                <div
                                    style={{
                                        display: 'flex',
                                        gap: '20px',
                                        marginBottom: '15px',
                                    }}
                                >
                                    <span>
                                        Status:
                                        <span
                                            style={{
                                                color: getStatusColor(
                                                    song.processingStatus
                                                ),
                                                fontWeight: 'bold',
                                                marginLeft: '5px',
                                            }}
                                        >
                                            {song.processingStatus}
                                        </span>
                                    </span>
                                    <span>
                                        Duration:{' '}
                                        {formatDuration(song.durationSeconds)}
                                    </span>
                                    {song.availableBitrates &&
                                        song.availableBitrates.length > 0 && (
                                            <span>
                                                Available:{' '}
                                                {song.availableBitrates.join(
                                                    ', '
                                                )}
                                            </span>
                                        )}
                                </div>

                                {/* Playback Controls */}
                                <div
                                    style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        gap: '10px',
                                    }}
                                >
                                    {/* MP3 Button */}
                                    <div>
                                        <button
                                            onClick={() => playMp3Song(song)}
                                            style={getMp3ButtonStyle(
                                                isCurrentSong &&
                                                    currentBitrate === 'MP3'
                                            )}
                                        >
                                            Play MP3 (Original)
                                        </button>
                                    </div>

                                    {/* HLS Bitrate Buttons */}
                                    {song.processingStatus === 'COMPLETED' &&
                                        song.availableBitrates &&
                                        song.availableBitrates.length > 0 && (
                                            <div>
                                                <div
                                                    style={{
                                                        fontSize: '14px',
                                                        marginBottom: '5px',
                                                        color: '#666',
                                                    }}
                                                >
                                                    HLS Bitrates:
                                                </div>
                                                <div
                                                    style={{
                                                        display: 'flex',
                                                        gap: '8px',
                                                        flexWrap: 'wrap',
                                                    }}
                                                >
                                                    {song.availableBitrates.map(
                                                        (bitrate) => (
                                                            <button
                                                                key={bitrate}
                                                                onClick={() =>
                                                                    playHlsBitrate(
                                                                        song,
                                                                        bitrate
                                                                    )
                                                                }
                                                                style={getBitrateButtonStyle(
                                                                    bitrate,
                                                                    isCurrentSong &&
                                                                        currentBitrate ===
                                                                            bitrate
                                                                )}
                                                            >
                                                                {bitrate}
                                                            </button>
                                                        )
                                                    )}
                                                </div>
                                            </div>
                                        )}

                                    {/* Processing Status */}
                                    {song.processingStatus === 'PROCESSING' && (
                                        <div
                                            style={{
                                                padding: '8px 12px',
                                                backgroundColor: '#fff3cd',
                                                color: '#856404',
                                                borderRadius: '4px',
                                                fontSize: '14px',
                                                border: '1px solid #ffeaa7',
                                            }}
                                        >
                                            🔄 Processing HLS variants...
                                        </div>
                                    )}

                                    {song.processingStatus === 'PENDING' && (
                                        <div
                                            style={{
                                                padding: '8px 12px',
                                                backgroundColor: '#f8f9fa',
                                                color: '#6c757d',
                                                borderRadius: '4px',
                                                fontSize: '14px',
                                                border: '1px solid #dee2e6',
                                            }}
                                        >
                                            ⏳ Waiting to process...
                                        </div>
                                    )}

                                    {song.processingStatus === 'FAILED' && (
                                        <div
                                            style={{
                                                padding: '8px 12px',
                                                backgroundColor: '#f8d7da',
                                                color: '#721c24',
                                                borderRadius: '4px',
                                                fontSize: '14px',
                                                border: '1px solid #f5c6cb',
                                            }}
                                        >
                                            ❌ Processing failed - MP3 only
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })
                )}
            </div>

            {/* Audio Player */}
            {currentSong && (
                <div
                    style={{
                        margin: '30px 0',
                        padding: '20px',
                        border: '2px solid #007bff',
                        borderRadius: '8px',
                        backgroundColor: '#f8f9fa',
                    }}
                >
                    <h3 style={{ margin: '0 0 15px 0' }}>
                        Now Playing: {currentSong.title}
                    </h3>
                    <div
                        style={{
                            marginBottom: '15px',
                            padding: '10px',
                            backgroundColor: '#e9ecef',
                            borderRadius: '4px',
                            fontSize: '14px',
                        }}
                    >
                        <strong>Format:</strong> {currentBitrate} |
                        <strong> Status:</strong> {currentSong.processingStatus}{' '}
                        |<strong> Duration:</strong>{' '}
                        {formatDuration(currentSong.durationSeconds)}
                    </div>
                    <audio
                        ref={audioRef}
                        controls
                        preload='auto'
                        style={{ width: '100%' }}
                        onError={(e) => {
                            console.error('Audio error:', e);
                            setError('Audio playback error');
                        }}
                        onLoadStart={() => console.log('Loading started')}
                        onCanPlay={() => console.log('Can play')}
                        onPlay={() => setError(null)}
                    />
                </div>
            )}

            <div style={{ marginTop: '30px' }}>
                <h3>Protected Resource Data:</h3>
                {protectedResourceData ? (
                    <div
                        style={{
                            backgroundColor: '#f8f9fa',
                            padding: '10px',
                            borderRadius: '4px',
                            fontFamily: 'monospace',
                        }}
                    >
                        {JSON.stringify(protectedResourceData, null, 2)}
                    </div>
                ) : (
                    <div>Loading protected resource data...</div>
                )}
            </div>
        </div>
    );
}

export default TestPage;
