import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { LogOut, User, Heart } from 'lucide-react';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-lg">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-2 text-2xl font-bold">
            <Heart className="w-8 h-8" />
            <span>Virtual Pets</span>
          </Link>

          <nav className="flex items-center space-x-6">
            {isAuthenticated ? (
              <>
                <Link 
                  to="/pets" 
                  className="hover:text-purple-200 transition-colors"
                >
                  My Pets
                </Link>
                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-2">
                    <User className="w-5 h-5" />
                    <span className="font-medium">{user.username}</span>
                    {user.role === 'ROLE_ADMIN' && (
                      <span className="bg-yellow-500 text-yellow-900 px-2 py-1 rounded-full text-xs font-bold">
                        ADMIN
                      </span>
                    )}
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center space-x-1 bg-purple-700 hover:bg-purple-800 px-3 py-2 rounded-lg transition-colors"
                  >
                    <LogOut className="w-4 h-4" />
                    <span>Logout</span>
                  </button>
                </div>
              </>
            ) : (
              <div className="flex items-center space-x-4">
                <Link 
                  to="/login" 
                  className="hover:text-purple-200 transition-colors"
                >
                  Login
                </Link>
                <Link 
                  to="/register" 
                  className="bg-purple-700 hover:bg-purple-800 px-4 py-2 rounded-lg transition-colors"
                >
                  Register
                </Link>
              </div>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;