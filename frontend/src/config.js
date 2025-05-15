const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
const supabaseUrl = process.env.REACT_APP_SUPABASE_URL;
const supabaseKey = process.env.REACT_APP_SUPABASE_KEY;
const bearerString = localStorage.getItem('sb-hxuegtmworlasjwdqsya-auth-token');
console.log('supa base url', supabaseUrl);
console.log('supa base key', supabaseKey);
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
