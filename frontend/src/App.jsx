import './App.css';
import gatewayUrl from './config';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
  // sent api call to the gateway url using Axios
  const [data, setData] = useState(null);
  useEffect(() => {
    axios.post(`${gatewayUrl}/demo/mqtt`, { data: 'test' })
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
    </div>
  );
}

export default App;
