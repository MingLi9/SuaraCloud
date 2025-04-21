import React from 'react';

export const HomeIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <path d='M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z'></path>
        <polyline points='9 22 9 12 15 12 15 22'></polyline>
    </svg>
);

export const SearchIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <circle cx='11' cy='11' r='8'></circle>
        <line x1='21' y1='21' x2='16.65' y2='16.65'></line>
    </svg>
);

export const LibraryIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <path d='M16 6l4 14'></path>
        <path d='M12 6v14'></path>
        <path d='M8 8v12'></path>
        <path d='M4 4v16'></path>
    </svg>
);

export const PlusIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <line x1='12' y1='5' x2='12' y2='19'></line>
        <line x1='5' y1='12' x2='19' y2='12'></line>
    </svg>
);

export const HeartIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <path d='M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z'></path>
    </svg>
);

export const ChevronLeftIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polyline points='15 18 9 12 15 6'></polyline>
    </svg>
);

export const ChevronRightIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polyline points='9 18 15 12 9 6'></polyline>
    </svg>
);

export const PlayIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='currentColor'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polygon points='5 3 19 12 5 21 5 3'></polygon>
    </svg>
);

export const SkipBackIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polygon points='19 20 9 12 19 4 19 20'></polygon>
        <line x1='5' y1='19' x2='5' y2='5'></line>
    </svg>
);

export const SkipForwardIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polygon points='5 4 15 12 5 20 5 4'></polygon>
        <line x1='19' y1='5' x2='19' y2='19'></line>
    </svg>
);

export const RepeatIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polyline points='17 1 21 5 17 9'></polyline>
        <path d='M3 11V9a4 4 0 0 1 4-4h14'></path>
        <polyline points='7 23 3 19 7 15'></polyline>
        <path d='M21 13v2a4 4 0 0 1-4 4H3'></path>
    </svg>
);

export const ShuffleIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polyline points='16 3 21 3 21 8'></polyline>
        <line x1='4' y1='20' x2='21' y2='3'></line>
        <polyline points='21 16 21 21 16 21'></polyline>
        <line x1='15' y1='15' x2='21' y2='21'></line>
        <line x1='4' y1='4' x2='9' y2='9'></line>
    </svg>
);

export const VolumeIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polygon points='11 5 6 9 2 9 2 15 6 15 11 19 11 5'></polygon>
        <path d='M15.54 8.46a5 5 0 0 1 0 7.07'></path>
        <path d='M19.07 4.93a10 10 0 0 1 0 14.14'></path>
    </svg>
);

export const MaximizeIcon = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='none'
        stroke='currentColor'
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
    >
        <polyline points='15 3 21 3 21 9'></polyline>
        <polyline points='9 21 3 21 3 15'></polyline>
        <line x1='21' y1='3' x2='14' y2='10'></line>
        <line x1='3' y1='21' x2='10' y2='14'></line>
    </svg>
);

export const SpotifyLogo = ({ className = 'icon' }) => (
    <svg
        className={className}
        xmlns='http://www.w3.org/2000/svg'
        viewBox='0 0 24 24'
        fill='currentColor'
    >
        <path d='M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z' />
    </svg>
);

export const DiscordLogo = ({ className = 'icon' }) => (
    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 127.14 96.36'>
        <path
            d='M107.7,8.07A105.15,105.15,0,0,0,81.47,0a72.06,72.06,0,0,0-3.36,6.83A97.68,97.68,0,0,0,49,6.83,72.37,72.37,0,0,0,45.64,0,105.89,105.89,0,0,0,19.39,8.09C2.79,32.65-1.71,56.6.54,80.21h0A105.73,105.73,0,0,0,32.71,96.36,77.7,77.7,0,0,0,39.6,85.25a68.42,68.42,0,0,1-10.85-5.18c.91-.66,1.8-1.34,2.66-2a75.57,75.57,0,0,0,64.32,0c.87.71,1.76,1.39,2.66,2a68.68,68.68,0,0,1-10.87,5.19,77,77,0,0,0,6.89,11.1A105.25,105.25,0,0,0,126.6,80.22h0C129.24,52.84,122.09,29.11,107.7,8.07ZM42.45,65.69C36.18,65.69,31,60,31,53s5-12.74,11.43-12.74S54,46,53.89,53,48.84,65.69,42.45,65.69Zm42.24,0C78.41,65.69,73.25,60,73.25,53s5-12.74,11.44-12.74S96.23,46,96.12,53,91.08,65.69,84.69,65.69Z'
            fill='currentColor'
        />
    </svg>
);
