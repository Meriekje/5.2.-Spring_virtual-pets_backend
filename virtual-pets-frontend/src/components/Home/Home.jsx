import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Heart, Zap, Coffee, Star, Users, Shield } from 'lucide-react';

const Home = () => {
  const { isAuthenticated } = useAuth();

  const features = [
    {
      icon: <Heart className="w-8 h-8 text-red-500" />,
      title: "Care & Love",
      description: "Feed, play, and nurture your virtual pets with love and attention."
    },
    {
      icon: <Zap className="w-8 h-8 text-yellow-500" />,
      title: "Interactive Actions",
      description: "Engage with your pets through various activities that affect their mood and stats."
    },
    {
      icon: <Coffee className="w-8 h-8 text-brown-500" />,
      title: "Real-time Stats",
      description: "Monitor happiness, energy, and hunger levels in real-time."
    },
    {
      icon: <Star className="w-8 h-8 text-purple-500" />,
      title: "Multiple Pet Types",
      description: "Choose from different pet types, each with unique characteristics."
    },
    {
      icon: <Users className="w-8 h-8 text-blue-500" />,
      title: "User Accounts",
      description: "Create your account and manage multiple pets with ease."
    },
    {
      icon: <Shield className="w-8 h-8 text-green-500" />,
      title: "Secure & Safe",
      description: "Your pets and data are protected with modern security measures."
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-100 via-pink-50 to-blue-100">
      {/* Hero Section */}
      <section className="py-20 px-4">
        <div className="container mx-auto text-center">
          <div className="mb-8">
            <div className="text-8xl mb-6">🐾</div>
            <h1 className="text-6xl font-bold text-gray-800 mb-6">
              Virtual <span className="text-transparent bg-clip-text bg-gradient-to-r from-purple-600 to-pink-600">Pets</span>
            </h1>
            <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
              Welcome to the most adorable virtual pet experience! Create, care for, and bond with your digital companions in this magical world.
            </p>
          </div>

          {!isAuthenticated ? (
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                to="/register"
                className="bg-gradient-to-r from-purple-600 to-pink-600 text-white px-8 py-4 rounded-lg font-semibold text-lg hover:from-purple-700 hover:to-pink-700 transition-all transform hover:scale-105"
              >
                Get Started
              </Link>
              <Link
                to="/login"
                className="border-2 border-purple-600 text-purple-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-purple-600 hover:text-white transition-all"
              >
                Sign In
              </Link>
            </div>
          ) : (
            <Link
              to="/pets"
              className="bg-gradient-to-r from-purple-600 to-pink-600 text-white px-8 py-4 rounded-lg font-semibold text-lg hover:from-purple-700 hover:to-pink-700 transition-all transform hover:scale-105 inline-block"
            >
              View My Pets
            </Link>
          )}
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 px-4 bg-white/50">
        <div className="container mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold text-gray-800 mb-4">Amazing Features</h2>
            <p className="text-xl text-gray-600">Everything you need to create the perfect pet experience</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div
                key={index}
                className="bg-white rounded-2xl p-8 shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-105"
              >
                <div className="mb-4">{feature.icon}</div>
                <h3 className="text-xl font-bold text-gray-800 mb-3">{feature.title}</h3>
                <p className="text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Pet Types Section */}
      <section className="py-20 px-4">
        <div className="container mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold text-gray-800 mb-4">Meet Your Future Companions</h2>
            <p className="text-xl text-gray-600">Choose from our adorable collection of virtual pets</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-gradient-to-br from-green-100 to-green-200 rounded-2xl p-8 text-center">
              <div className="text-6xl mb-4">🦔</div>
              <h3 className="text-2xl font-bold text-gray-800 mb-3">Mole</h3>
              <p className="text-gray-600">Great digger with high endurance. Perfect for adventurous pet owners!</p>
            </div>

            <div className="bg-gradient-to-br from-blue-100 to-blue-200 rounded-2xl p-8 text-center">
              <div className="text-6xl mb-4">🐦</div>
              <h3 className="text-2xl font-bold text-gray-800 mb-3">Magpie</h3>
              <p className="text-gray-600">Loves shiny things and is very energetic. Always ready for fun and games!</p>
            </div>

            <div className="bg-gradient-to-br from-purple-100 to-purple-200 rounded-2xl p-8 text-center">
              <div className="text-6xl mb-4">🐸</div>
              <h3 className="text-2xl font-bold text-gray-800 mb-3">Toad</h3>
              <p className="text-gray-600">Balanced and adaptable companion. Great for beginners and experts alike!</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      {!isAuthenticated && (
        <section className="py-20 px-4 bg-gradient-to-r from-purple-600 to-pink-600">
          <div className="container mx-auto text-center">
            <h2 className="text-4xl font-bold text-white mb-6">Ready to Start Your Journey?</h2>
            <p className="text-xl text-purple-100 mb-8 max-w-2xl mx-auto">
              Join thousands of pet lovers and create your first virtual companion today!
            </p>
            <Link
              to="/register"
              className="bg-white text-purple-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition-all transform hover:scale-105 inline-block"
            >
              Create Your Account
            </Link>
          </div>
        </section>
      )}
    </div>
  );
};

export default Home;