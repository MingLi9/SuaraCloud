import gatewayUrl from '../config';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { headers } from '../config';

function TestPage() {
    // sent api call to the gateway url using Axios
    const [data, setData] = useState(null);
    const [protectedResourceData, setProtectedResourceData] = useState(null);
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

        // Check protected resource, only logged in users can access this resource
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
    }, []);

    return (
        <div className='App'>
            <div data-testid='gateway_url'>gateway_url = {gatewayUrl}</div>
            data = {data}
            <audio controls preload='auto' src={song_url} />
            <div>Protected Resource Data:</div>
            {protectedResourceData ? (
                <div>{JSON.stringify(protectedResourceData)}</div>
            ) : (
                <div>Loading protected resource data...</div>
            )}
        </div>
    );
}

export default TestPage;
