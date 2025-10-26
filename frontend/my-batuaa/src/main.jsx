import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
//import 'bootstrap/dist/css/bootstrap.min.css';

//import './index.css'

// 🧩 Import MUI theme utilities
import { ThemeProvider, createTheme } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
