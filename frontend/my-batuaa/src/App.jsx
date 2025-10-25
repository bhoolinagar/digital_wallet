import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import './App.css'
import Dashboard from './dashboard/Dashbaord.jsx'
import { BrowserRouter, Route,Router,Routes } from 'react-router-dom'
import WalletList from './dashboard/WalletList.jsx'
import AddMoney from './dashboard/AddMoney.jsx'
import AddWallet from './dashboard/AddWallet.jsx'

function App() {
  const [count, setCount] = useState(0)

   {/*   <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} /> */}
  return (
    <BrowserRouter> 
    <Welcomepage />
    <Routes>  
      <Route path='/' element={<Dashboard></Dashboard>}/>
      <Route  path='/walletlist' element={<WalletList/>}></Route>
      <Route  path='/addmoney/:walletId' element={<AddMoney/>}/>
      <Route path='/addwallet' element={<AddWallet/>}></Route>
   <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
   
      <Routes>
  
    </BrowserRouter>
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
