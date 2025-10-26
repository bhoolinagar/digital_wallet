import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react({
      server: {
    port: 3000,        // set your default frontend port here
    open: true,        // automatically open the browser
  },
      // babel: {
      //   plugins: [['babel-plugin-react-compiler']],
      // },
    }),
  ],
})
