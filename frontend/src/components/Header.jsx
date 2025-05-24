import React from 'react';
import { ChevronLeftIcon, ChevronRightIcon } from './Icons';

const Header = ({ logout }) => {
    return (
        <div className='header'>
            <div className='header-nav'>
                <button className='header-button'>
                    <ChevronLeftIcon />
                </button>
                <button className='header-button'>
                    <ChevronRightIcon />
                </button>
            </div>
            <div className='header-user'>
                <button className='upgrade-button' onClick={logout}>
                    Logout
                </button>
                <button className='upgrade-button'>Upgrade</button>
                <div className='user-avatar'>U</div>
            </div>
        </div>
    );
};

export default Header;
