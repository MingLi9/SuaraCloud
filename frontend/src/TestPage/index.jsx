import gatewayUrl from '../config';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function TestPage() {
    // sent api call to the gateway url using Axios
    const [data, setData] = useState(null);
    const song_url = `${gatewayUrl}/songs/toto`;
    useEffect(() => {
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
    }, []);

    return (
        <div className='App'>
            <div data-testid='gateway_url'>gateway_url = {gatewayUrl}</div>
            data = {data}
            <audio controls preload='auto' src={song_url} />
        </div>
    );
}

export default TestPage;
