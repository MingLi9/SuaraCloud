import './App.css';
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Home';
import TestPage from './TestPage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route index element={<Home />} />
                <Route path='/test' element={<TestPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
