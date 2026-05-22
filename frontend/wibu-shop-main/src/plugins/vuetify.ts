import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

import { createVuetify } from 'vuetify'

const popFigureLight = {
  dark: false,
  colors: {
    background: '#f5efe6',
    surface: '#fffaf3',
    'surface-bright': '#ffffff',
    'surface-light': '#fff7ed',
    'surface-variant': '#e8ddd1',
    'on-surface-variant': '#4b3f35',
    primary: '#c44f2d',
    'primary-darken-1': '#a33f20',
    secondary: '#356d6b',
    'secondary-darken-1': '#2d5b59',
    accent: '#d9a441',
    error: '#b42318',
    info: '#1d6fa5',
    success: '#2d7a4b',
    warning: '#b9770e',
    'on-primary': '#fffaf3',
    'on-secondary': '#fffaf3',
  },
  variables: {
    'border-color': '#e5d8c9',
    'border-opacity': 0.42,
    'high-emphasis-opacity': 0.92,
    'medium-emphasis-opacity': 0.72,
    'disabled-opacity': 0.34,
    'idle-opacity': 0.06,
    'hover-opacity': 0.08,
    'focus-opacity': 0.14,
    'selected-opacity': 0.12,
    'activated-opacity': 0.12,
    'pressed-opacity': 0.12,
    'dragged-opacity': 0.08,
  },
}

const popFigureDark = {
  dark: true,
  colors: {
    background: '#17110d',
    surface: '#211813',
    'surface-bright': '#2a1f19',
    'surface-light': '#281d17',
    'surface-variant': '#3a2b22',
    'on-surface-variant': '#d8c8ba',
    primary: '#f68b62',
    'primary-darken-1': '#d46f46',
    secondary: '#6ea7a2',
    'secondary-darken-1': '#4f8984',
    accent: '#efc15b',
    error: '#ff7a6d',
    info: '#74b5dd',
    success: '#6cc98a',
    warning: '#f0b15f',
    'on-primary': '#1a120e',
    'on-secondary': '#0e1414',
  },
  variables: {
    'border-color': '#4a3930',
    'border-opacity': 0.36,
    'high-emphasis-opacity': 0.92,
    'medium-emphasis-opacity': 0.72,
    'disabled-opacity': 0.34,
    'idle-opacity': 0.08,
    'hover-opacity': 0.08,
    'focus-opacity': 0.14,
    'selected-opacity': 0.12,
    'activated-opacity': 0.12,
    'pressed-opacity': 0.12,
    'dragged-opacity': 0.08,
  },
}

export default createVuetify({
  theme: {
    defaultTheme: 'popFigureLight',
    themes: {
      popFigureLight,
      popFigureDark,
    },
  },
  defaults: {
    VBtn: {
      style: 'text-transform: none; letter-spacing: 0;',
      rounded: 'lg',
    },
    VCard: {
      rounded: 'xl',
    },
  },
})
