import { useState } from 'react'
import './App.css'
import Dashboard from './component/Dashbaord.jsx'
import { BrowserRouter, Route,Router,Routes } from 'react-router-dom'
import RegisterPage from './component/Register.jsx';
import Login from './component/Login.jsx';
import TransferMoney from './transferMoney/transferMoney.jsx'
import WalletList from './component/WalletList.jsx'
import AddMoney from './component/AddMoney.jsx'
import AddWallet from './component/AddWallet.jsx'
import Navbar from './Navbar.jsx'
import Welcomepage from './landing/WelcomePage.jsx';
function App() {
  const [count, setCount] = useState(0)

   {/*   <Route path="/" element={<Welcomepage/>} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} /> */}
  
return (
    <BrowserRouter>  
    <Routes>
       <Route path="/" element={<Welcomepage/>} />
      <Route path='/dashboard' element={<Dashboard></Dashboard>}/>
      <Route  path='/walletlist' element={<WalletList/>}></Route>
      <Route  path='/addmoney/:walletId' element={<AddMoney/>}/>
      <Route path='/addwallet' element={<AddWallet/>}></Route>
      <Route path='/transferMoney' element={<TransferMoney />}></Route>
      <Route path='/addwallet' element={<AddWallet/>}></Route>
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/login" element={<Login />} />
     
     </Routes>
    </BrowserRouter>
  )
}

export default App
