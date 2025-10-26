import { useState } from 'react'

import './App.css'
import Dashboard from './component/Dashbaord.jsx'
import { BrowserRouter, Route,Router,Routes } from 'react-router-dom'
import TransferMoney from './transferMoney/TransferMoney.jsx'
import WalletList from './component/WalletList.jsx'
import AddMoney from './component/AddMoney.jsx'
import AddWallet from './component/AddWallet.jsx'
import Navbar from './Navbar.jsx'
function App() {
  const [count, setCount] = useState(0)

   {/*   <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} /> */}
  return (
    <BrowserRouter> 
     <Navbar />
    <Routes>
      <Route path='/' element={<Dashboard></Dashboard>}/>
      <Route  path='/walletlist' element={<WalletList/>}></Route>
      <Route  path='/addmoney/:walletId' element={<AddMoney/>}/>
      <Route path='/addwallet/:emailId' element={<AddWallet/>}></Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
