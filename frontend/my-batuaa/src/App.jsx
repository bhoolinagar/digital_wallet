import { useState } from 'react'

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
    <Routes>  
      <Route path='/' element={<Dashboard></Dashboard>}/>
      <Route  path='/walletlist' element={<WalletList/>}></Route>
      <Route  path='/addmoney/:walletId' element={<AddMoney/>}/>
      <Route path='/addwallet' element={<AddWallet/>}></Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
