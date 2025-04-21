import React from 'react';
import {
    HomeIcon,
    SearchIcon,
    LibraryIcon,
    PlusIcon,
    HeartIcon,
} from './Icons';

const Sidebar = () => {
    return (
        <div className='sidebar'>
            <div className='sidebar-section'>
                <button className='sidebar-nav-item'>
                    <HomeIcon />
                    <span>Home</span>
                </button>
                <button className='sidebar-nav-item'>
                    <SearchIcon />
                    <span>Search</span>
                </button>
            </div>

            <div className='sidebar-section library'>
                <div className='sidebar-header'>
                    <button className='sidebar-nav-item'>
                        <LibraryIcon />
                        <span>Your Library</span>
                    </button>
                    <button className='sidebar-nav-item'>
                        <PlusIcon className='icon-sm' />
                    </button>
                </div>

                <div className='playlists'>
                    <div className='playlist-item'>
                        <div className='playlist-cover liked-songs-cover'>
                            <HeartIcon />
                        </div>
                        <div className='playlist-info'>
                            <p className='playlist-title'>Liked Songs</p>
                            <p className='playlist-subtitle'>
                                Playlist • 123 songs
                            </p>
                        </div>
                    </div>

                    {Array.from({ length: 10 }).map((_, i) => (
                        <div key={i} className='playlist-item'>
                            <div className='playlist-cover'>P{i + 1}</div>
                            <div className='playlist-info'>
                                <p className='playlist-title'>
                                    Playlist {i + 1}
                                </p>
                                <p className='playlist-subtitle'>
                                    Playlist • User
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Sidebar;
