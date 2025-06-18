const gatewayUrl = process.env.REACT_APP_GATEWAY_URL || `http://localhost:8085`;
const supabaseUrl = process.env.REACT_APP_SUPABASE_URL;
const supabaseKey = process.env.REACT_APP_SUPABASE_KEY;
const bearerString = localStorage.getItem('sb-hxuegtmworlasjwdqsya-auth-token');
let accessToken = '';
if (bearerString) {
    const bearerObject = JSON.parse(bearerString);
    accessToken = bearerObject.access_token;
}
const headers = {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${accessToken}`,
};

export default gatewayUrl;
export { supabaseUrl, supabaseKey, headers };
