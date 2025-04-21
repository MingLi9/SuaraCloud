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
