import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import './App.css'
import Dashboard from './dashboard/Dashbaord.jsx'
import Welcomepage from './landingpage/Welcomepage.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
      <Welcomepage />
      <Routes>
        <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </Router>
  
    // <div>
    //   {/* <h3>Home page</h3> */}
    //   {/* <Dashboard /> */}
    //   <Welcomepage/>
    //   <LoginPage/>
    //   <RegisterPage/>
    // </div>
  )
}

export default App
