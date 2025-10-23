import { useState } from 'react'

import './App.css'
import Dashboard from './dashboard/Dashbaord.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div>
      <h3>Home page</h3>
      <Dashboard />
    </div>
  )
}

export default App
