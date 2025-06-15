import http from 'k6/http';
import { sleep, check } from 'k6';
// const base_url = 'http://127.0.0.1/gateway';
const base_url = 'http://localhost:8085';
const token =
    'eyJhbGciOiJIUzI1NiIsImtpZCI6Ik1YUE0xcEhrUW56VmJqdEgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2h4dWVndG13b3JsYXNqd2Rxc3lhLnN1cGFiYXNlLmNvL2F1dGgvdjEiLCJzdWIiOiI0Y2NlOTlkYS0yNjUwLTRmMzctYWM5YS1lNDVkODhiNmYxOTYiLCJhdWQiOiJhdXRoZW50aWNhdGVkIiwiZXhwIjoxNzQ3MDU1MzMwLCJpYXQiOjE3NDcwNTE3MzAsImVtYWlsIjoiamFuc3Nlbm1pbmdAZ21haWwuY29tIiwicGhvbmUiOiIiLCJhcHBfbWV0YWRhdGEiOnsicHJvdmlkZXIiOiJlbWFpbCIsInByb3ZpZGVycyI6WyJlbWFpbCJdfSwidXNlcl9tZXRhZGF0YSI6eyJlbWFpbF92ZXJpZmllZCI6dHJ1ZX0sInJvbGUiOiJhdXRoZW50aWNhdGVkIiwiYWFsIjoiYWFsMSIsImFtciI6W3sibWV0aG9kIjoicGFzc3dvcmQiLCJ0aW1lc3RhbXAiOjE3NDcwNTE3MzB9XSwic2Vzc2lvbl9pZCI6IjgxNjliNjE3LTE1OTQtNDk3Yi1iNTgzLTJkMGJlNGM2MTBmYSIsImlzX2Fub255bW91cyI6ZmFsc2V9.yA2cAfm40DvbpBmMQk3Xl1w4BjN-TytH4jQCsXVEPhc';

const headers = {
    Authorization: `Bearer ${token}`,
    'Content-Type': 'application/json',
    'User-Agent': 'PostmanRuntime/7.39.0',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'en-US,en;q=0.9',
    Connection: 'keep-alive',
    Accept: 'application/json',
};

export const options = {
    stages: [
        { duration: '5m', target: 100 }, // ramp-up from 1 to 100 users
        { duration: '10m', target: 100 }, // stay at 100 users
        { duration: '5m', target: 0 }, // ramp-down to 0 users
    ],
};

export default function () {
    // sleep(1); // give the gateway a moment to fully initialize

    let songmeta = http.get(`${base_url}/songmeta/health`, { headers });
    console.log(`SongMeta status: ${songmeta.status}, body: ${songmeta.body}`);
    check(songmeta, { 'SongMeta status is 200': (res) => res.status === 200 });

    // sleep(0.5);

    // let songList = http.get(`${base_url}/songs/list`, { headers });
    // console.log(`SongList status: ${songList.status}, body: ${songList.body}`);
    // check(songList, { 'Song list status is 200': (res) => res.status === 200 });

    // sleep(0.5);

    // let resSong = http.get(
    //     `${base_url}/songs/74cb0f50-ff9f-4193-a6e5-dc22bfb55119.mp3`,
    //     { headers }
    // );
    // console.log(`resSong status: ${resSong.status}`);
    // check(resSong, { 'Songs status is 200': (res) => res.status === 200 });

    // sleep(1);
}
