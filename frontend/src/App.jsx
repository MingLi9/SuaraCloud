import './App.css';
import gatewayUrl from './config';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
  // sent api call to the gateway url using Axios
  const [data, setData] = useState(null);
  useEffect(() => {
    axios.post(`${gatewayUrl}/demo/mqtt`, "test", {
      headers: {
        "Content-Type": "application/json",
      }
    })
      .then(response => {
        setData(response.data);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []);

  return (
    <div className="App">
      gateway_url = {gatewayUrl}
      data = {data}
      <audio controls preload='auto' src='https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3' />
    </div>
  );
}

export default App;
