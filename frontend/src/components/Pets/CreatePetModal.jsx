import React, { useState } from 'react';
import { X, Plus } from 'lucide-react';
import { petsAPI } from '../../services/api';

const CreatePetModal = ({ isOpen, onClose, onPetCreated }) => {
  const [formData, setFormData] = useState({
    name: '',
    type: 'MOLE',
    color: '#FF6B6B',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const petTypes = [
    { value: 'MOLE', label: 'Mole 🦔', description: 'Great digger, high endurance' },
    { value: 'MAGPIE', label: 'Magpie 🐦', description: 'Loves shiny things, very energetic' },
    { value: 'TOAD', label: 'Toad 🐸', description: 'Balanced and adaptable' },
  ];

  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
    '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
  ];

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await petsAPI.create(formData);
      onPetCreated(response.data);
      onClose();
      setFormData({ name: '', type: 'MOLE', color: '#FF6B6B' });
    } catch (error) {
      setError(error.response?.data?.message || 'Failed to create pet');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-2xl shadow-2xl p-6 w-full max-w-md">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Create New Pet</h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Pet Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              minLength={2}
              maxLength={50}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
              placeholder="Enter pet name"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Pet Type
            </label>
            <div className="space-y-2">
              {petTypes.map((type) => (
                <label key={type.value} className="flex items-center space-x-3 p-3 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer">
                  <input
                    type="radio"
                    name="type"
                    value={type.value}
                    checked={formData.type === type.value}
                    onChange={handleChange}
                    className="text-purple-600 focus:ring-purple-500"
                  />
                  <div>
                    <div className="font-medium">{type.label}</div>
                    <div className="text-sm text-gray-600">{type.description}</div>
                  </div>
                </label>
              ))}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Pet Color
            </label>
            <div className="grid grid-cols-5 gap-2">
              {colors.map((color) => (
                <button
                  key={color}
                  type="button"
                  onClick={() => setFormData({ ...formData, color })}
                  className={`w-12 h-12 rounded-lg border-2 transition-all ${
                    formData.color === color 
                      ? 'border-gray-800 scale-110' 
                      : 'border-gray-300 hover:border-gray-400'
                  }`}
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>
          </div>

          <div className="flex space-x-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-3 px-4 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-gradient-to-r from-purple-600 to-pink-600 text-white py-3 px-4 rounded-lg font-semibold hover:from-purple-700 hover:to-pink-700 transition-all disabled:opacity-50"
            >
              {loading ? 'Creating...' : 'Create Pet'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreatePetModal;