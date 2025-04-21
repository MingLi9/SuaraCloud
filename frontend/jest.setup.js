require('@testing-library/jest-dom');
require('dotenv').config({ path: '.env.test' });
const { TextEncoder, TextDecoder } = require('util');
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;
