import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import './App.css'
import Dashboard from './dashboard/Dashbaord.jsx'
import Welcomepage from './landingpage/Welcomepage.jsx'
import Footer from './Footer/Footer.jsx'
import Transactionhistory from './History/Transactionhistory.jsx';

function App() {
  return (
  <>
    {/* <Router>
      <Routes>
        <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
      
        <Footer/>
    </Router> */}
    <Transactionhistory/>
  </>
     
    )
}

export default App
