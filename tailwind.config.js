/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html',
    './src/main/resources/static/js/**/*.js'
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#fff8ed',
          100: '#ffefd6',
          200: '#ffdbaa',
          300: '#ffc074',
          400: '#f59e0b',
          500: '#b45309',
          600: '#92400e',
          700: '#78350f',
          800: '#4e2a0e',
          900: '#3b210b'
        }
      }
    }
  },
  plugins: []
}


