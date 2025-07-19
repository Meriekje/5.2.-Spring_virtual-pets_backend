# Virtual Pets Frontend

A modern React frontend for the Virtual Pets application built with Vite, React, and Tailwind CSS.

## 🚀 Features

- **Modern React 18** with hooks and context
- **Vite** for fast development and building
- **Tailwind CSS** for beautiful, responsive styling
- **React Router** for navigation
- **Axios** for API communication
- **JWT Authentication** with automatic token management
- **Responsive Design** for mobile and desktop
- **Beautiful UI/UX** with animations and gradients

## 🛠️ Tech Stack

- **React 18** - UI library
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client
- **Lucide React** - Beautiful icons

## 📋 Prerequisites

- Node.js 18+ 
- npm or yarn
- Backend API running on `http://localhost:8080`

## 🔧 Installation

1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd virtual-pets-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Configure environment:**
   ```bash
   cp .env.example .env
   # Edit .env with your backend URL
   ```

4. **Start development server:**
   ```bash
   npm run dev
   ```

5. **Open in browser:**
   ```
   http://localhost:5173
   ```

## 🏗️ Project Structure

```
virtual-pets-frontend/
├── public/                 # Static assets
├── src/
│   ├── components/        # React components
│   │   ├── Auth/         # Authentication components
│   │   ├── Home/         # Home page components
│   │   ├── Layout/       # Layout components
│   │   └── Pets/         # Pet management components
│   ├── context/          # React context providers
│   ├── services/         # API services
│   ├── App.jsx           # Main app component
│   ├── main.jsx          # Entry point
│   └── index.css         # Global styles
├── .env.example          # Environment variables template
├── package.json          # Dependencies and scripts
├── tailwind.config.js    # Tailwind configuration
└── vite.config.js        # Vite configuration
```

## 🔌 Backend Connection

The frontend connects to your Spring Boot backend API. Make sure:

1. **Backend is running** on `http://localhost:8080`
2. **CORS is configured** in your Spring Boot app
3. **Environment variables** are set correctly

### API Endpoints Used:
- `POST /api/auth/login` - User authentication
- `POST /api/auth/register` - User registration
- `GET /api/pets` - Get user's pets
- `POST /api/pets` - Create new pet
- `PUT /api/pets/{id}` - Update pet
- `DELETE /api/pets/{id}` - Delete pet
- `POST /api/pets/{id}/feed` - Feed pet
- `POST /api/pets/{id}/play` - Play with pet
- `POST /api/pets/{id}/rest` - Rest pet

## 🎨 Customization

### Styling:
- Edit `tailwind.config.js` for theme customization
- Modify `src/index.css` for global styles
- Component styles use Tailwind utility classes

### API Configuration:
- Update `src/services/api.js` for API endpoints
- Modify base URL in environment variables

## 📱 Features

### Authentication:
- ✅ User registration and login
- ✅ JWT token management
- ✅ Automatic token refresh
- ✅ Protected routes

### Pet Management:
- ✅ View all pets in beautiful cards
- ✅ Create new pets with customization
- ✅ Real-time stat monitoring
- ✅ Interactive pet actions (feed, play, rest)
- ✅ Delete pets with confirmation

### UI/UX:
- ✅ Responsive design
- ✅ Beautiful gradients and animations
- ✅ Loading states and error handling
- ✅ Modern card-based layout

## 🚀 Available Scripts

```bash
# Development
npm run dev          # Start development server

# Building
npm run build        # Build for production
npm run preview      # Preview production build

# Linting
npm run lint         # Run ESLint
```

## 🌐 Environment Variables

Create a `.env` file in the root directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## 🔧 IntelliJ IDEA Setup

1. **Open project** in IntelliJ IDEA
2. **Install plugins:**
   - JavaScript and TypeScript
   - Tailwind CSS
   - React
3. **Configure Node.js** interpreter
4. **Enable ESLint** in settings
5. **Set up run configurations** for npm scripts

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Troubleshooting

### Common Issues:

**CORS Errors:**
- Ensure backend has CORS configured for `http://localhost:5173`

**API Connection Failed:**
- Check if backend is running on `http://localhost:8080`
- Verify environment variables

**Build Errors:**
- Clear node_modules: `rm -rf node_modules && npm install`
- Check Node.js version compatibility

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review backend API documentation
3. Check browser console for errors