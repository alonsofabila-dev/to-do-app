/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 8080,
  },
  test: {
    globals: true,
    environment: "jsdom",
    css: true,
    setupFiles: './frontend-to-do-app/setupTests.js',
  }
})
