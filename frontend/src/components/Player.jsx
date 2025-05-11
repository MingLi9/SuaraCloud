import React from 'react';
import {
    PlayIcon,
    SkipBackIcon,
    SkipForwardIcon,
    RepeatIcon,
    ShuffleIcon,
    HeartIcon,
    VolumeIcon,
    MaximizeIcon,
} from './Icons';

const Player = ({ currentSong }) => {
    return (
        <div className='player'>
            <div className='player-left'>
                <div className='current-song-cover'>
                    {/* {currentSong.title[0]} */}
                </div>
                <div className='current-song-info'>
                    <p className='current-song-title'>
                        {/* {currentSong.title} */}
                        Witte chocolade vlokken
                    </p>
                    <p className='current-song-artist'>
                        {/* {currentSong.artist} */}
                        Piet
                    </p>
                </div>
                <button className='like-button'>
                    <HeartIcon className='icon-sm' />
                </button>
            </div>

            <div className='player-center'>
                <div className='player-controls'>
                    <button className='control-button'>
                        <ShuffleIcon className='icon-sm' />
                    </button>
                    <button className='control-button'>
                        <SkipBackIcon className='icon-sm' />
                    </button>
                    <button className='control-button play-control'>
                        <PlayIcon className='icon-sm' />
                    </button>
                    <button className='control-button'>
                        <SkipForwardIcon className='icon-sm' />
                    </button>
                    <button className='control-button'>
                        <RepeatIcon className='icon-sm' />
                    </button>
                </div>

                <div className='progress-container'>
                    <span className='progress-time'>1:20</span>
                    <div className='progress-bar'>
                        <div className='progress-fill'></div>
                    </div>
                    <span className='progress-time'>
                        {/* {currentSong.duration} */}
                        3:45
                    </span>
                </div>
            </div>

            <div className='player-right'>
                <button className='control-button'>
                    <VolumeIcon className='icon-sm' />
                </button>
                <div className='volume-bar'>
                    <div className='volume-fill'></div>
                </div>
                <button className='control-button'>
                    <MaximizeIcon className='icon-sm' />
                </button>
            </div>
        </div>
    );
};

export default Player;
