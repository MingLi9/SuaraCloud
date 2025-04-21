import http from 'k6/http';
import { sleep, check } from 'k6';
const base_url = 'http://localhost:8085';

export const options = {
  stages: [
    { duration: '60s', target: 100 }, // ramp-up from 1 to 1000 users
    { duration: '5m', target: 100 },  // stay at 1000 users
    { duration: '30s', target: 0 },    // ramp-down to 0 users
  ],
};

export default function() {
  // Initial GET request
  let resWelcome = http.get(`${base_url}/demo/welcome`);
  check(resWelcome, { "welcome status is 200": (res) => res.status === 200 });

  // POST request with body "k6 test"
  let resMqtt = http.post(`${base_url}/demo/mqtt`, "k6 test", {
    headers: { 'Content-Type': 'text/plain' }
  });
  check(resMqtt, { "mqtt status is 200": (res) => res.status === 200 });

  // Another GET request to /songs/toto
  let resSongs = http.get(`${base_url}/songs/toto`);
  check(resSongs, { "songs status is 200": (res) => res.status === 200 });

  sleep(1);
}
