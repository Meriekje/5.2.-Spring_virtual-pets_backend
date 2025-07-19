import React, { useState } from 'react';
import { Heart, Zap, Coffee, Bed, Trash2 } from 'lucide-react';
import { petsAPI } from '../../services/api';

const PetCard = ({ pet, onUpdate, onDelete }) => {
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(null);

  const handleAction = async (action, petId) => {
    setActionLoading(action);
    try {
      let response;
      switch (action) {
        case 'feed':
          response = await petsAPI.feed(petId);
          break;
        case 'play':
          response = await petsAPI.play(petId);
          break;
        case 'rest':
          response = await petsAPI.rest(petId);
          break;
        default:
          return;
      }
      onUpdate(response.data);
    } catch (error) {
      console.error(`Error ${action}ing pet:`, error);
    } finally {
      setActionLoading(null);
    }
  };

  const handleDelete = async () => {
    if (window.confirm(`Are you sure you want to delete ${pet.name}?`)) {
      setLoading(true);
      try {
        await petsAPI.delete(pet.id);
        onDelete(pet.id);
      } catch (error) {
        console.error('Error deleting pet:', error);
      } finally {
        setLoading(false);
      }
    }
  };

  const getStatusColor = (level) => {
    if (level >= 70) return 'bg-green-500';
    if (level >= 40) return 'bg-yellow-500';
    return 'bg-red-500';
  };

  const getStatusText = (level) => {
    if (level >= 70) return 'Great';
    if (level >= 40) return 'Okay';
    return 'Poor';
  };

  const getPetEmoji = (type) => {
    switch (type) {
      case 'MOLE': return '🦔';
      case 'MAGPIE': return '🐦';
      case 'TOAD': return '🐸';
      default: return '🐾';
    }
  };

  const getPetMood = () => {
    const avgLevel = (pet.happinessLevel + pet.energyLevel + (100 - pet.hungerLevel)) / 3;
    if (avgLevel >= 70) return '😊';
    if (avgLevel >= 40) return '😐';
    return '😢';
  };

  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 hover:shadow-xl transition-all duration-300 transform hover:scale-105">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-3">
          <div className="text-4xl">{getPetEmoji(pet.type)}</div>
          <div>
            <h3 className="text-xl font-bold text-gray-800">{pet.name}</h3>
            <p className="text-sm text-gray-600">{pet.type}</p>
          </div>
        </div>
        <div className="flex items-center space-x-2">
          <div className="text-2xl">{getPetMood()}</div>
          <div className="flex space-x-1">
            <button
              onClick={handleDelete}
              disabled={loading}
              className="p-2 text-red-500 hover:bg-red-50 rounded-lg transition-colors"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>

      {/* Stats */}
      <div className="space-y-3 mb-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Heart className="w-4 h-4 text-red-500" />
            <span className="text-sm font-medium">Happiness</span>
          </div>
          <span className="text-sm text-gray-600">{getStatusText(pet.happinessLevel)}</span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2">
          <div
            className={`h-2 rounded-full transition-all duration-500 ${getStatusColor(pet.happinessLevel)}`}
            style={{ width: `${pet.happinessLevel}%` }}
          ></div>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Zap className="w-4 h-4 text-yellow-500" />
            <span className="text-sm font-medium">Energy</span>
          </div>
          <span className="text-sm text-gray-600">{getStatusText(pet.energyLevel)}</span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2">
          <div
            className={`h-2 rounded-full transition-all duration-500 ${getStatusColor(pet.energyLevel)}`}
            style={{ width: `${pet.energyLevel}%` }}
          ></div>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Coffee className="w-4 h-4 text-brown-500" />
            <span className="text-sm font-medium">Hunger</span>
          </div>
          <span className="text-sm text-gray-600">{getStatusText(100 - pet.hungerLevel)}</span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2">
          <div
            className={`h-2 rounded-full transition-all duration-500 ${getStatusColor(100 - pet.hungerLevel)}`}
            style={{ width: `${100 - pet.hungerLevel}%` }}
          ></div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="grid grid-cols-3 gap-2">
        <button
          onClick={() => handleAction('feed', pet.id)}
          disabled={actionLoading === 'feed'}
          className="flex items-center justify-center space-x-1 bg-green-500 hover:bg-green-600 text-white py-2 px-3 rounded-lg transition-colors disabled:opacity-50"
        >
          <Coffee className="w-4 h-4" />
          <span className="text-sm">Feed</span>
        </button>
        
        <button
          onClick={() => handleAction('play', pet.id)}
          disabled={actionLoading === 'play'}
          className="flex items-center justify-center space-x-1 bg-blue-500 hover:bg-blue-600 text-white py-2 px-3 rounded-lg transition-colors disabled:opacity-50"
        >
          <Heart className="w-4 h-4" />
          <span className="text-sm">Play</span>
        </button>
        
        <button
          onClick={() => handleAction('rest', pet.id)}
          disabled={actionLoading === 'rest'}
          className="flex items-center justify-center space-x-1 bg-purple-500 hover:bg-purple-600 text-white py-2 px-3 rounded-lg transition-colors disabled:opacity-50"
        >
          <Bed className="w-4 h-4" />
          <span className="text-sm">Rest</span>
        </button>
      </div>
    </div>
  );
};

export default PetCard;