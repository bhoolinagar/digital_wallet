import { useState } from 'react'

import './App.css'
import Dashboard from './dashboard/Dashbaord.jsx'
import { BrowserRouter, Route,Router,Routes } from 'react-router-dom'
import WalletList from './dashboard/WalletList.jsx'
import AddMoney from './dashboard/AddMoney.jsx'
import AddWallet from './dashboard/AddWallet.jsx'
import TransferMoney from './transferMoney/TransferMoney.jsx'
import { AdminDashboard } from './adminDashboard/AdminDashboard.jsx'
import { AdminLayout } from './adminDashboard/AdminLayout.jsx'
import { Reports } from './adminDashboard/Reports.jsx'

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
      <Route path='/TransferMoney' element={<TransferMoney />}></Route>
      {/* <Route path= '/adminDashboard' element={<AdminDashboard />}></Route> */}


      {/* Admin routes inside layout */}
      <Route path="/admin" element={<AdminLayout />}>
      <Route index element={<AdminDashboard />} />  
      <Route path="dashboard" element={<AdminDashboard />} />
      <Route path="reports" element={<Reports />} />
      </Route>
       
      </Routes>
    </BrowserRouter>
  )
}

export default App
