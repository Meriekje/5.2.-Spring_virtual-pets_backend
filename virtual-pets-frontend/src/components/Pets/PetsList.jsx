import React, { useState, useEffect } from 'react';
import { Plus, RefreshCw } from 'lucide-react';
import { petsAPI } from '../../services/api';
import PetCard from './PetCard';
import CreatePetModal from './CreatePetModal';

const PetsList = () => {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const fetchPets = async () => {
    try {
      setLoading(true);
      const response = await petsAPI.getAll();
      setPets(response.data);
      setError('');
    } catch (error) {
      setError('Failed to load pets');
      console.error('Error fetching pets:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPets();
  }, []);

  const handlePetUpdate = (updatedPet) => {
    setPets(pets.map(pet => 
      pet.id === updatedPet.id ? updatedPet : pet
    ));
  };

  const handlePetDelete = (petId) => {
    setPets(pets.filter(pet => pet.id !== petId));
  };

  const handlePetCreated = (newPet) => {
    setPets([...pets, newPet]);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-100 to-pink-100 flex items-center justify-center">
        <div className="text-center">
          <RefreshCw className="w-12 h-12 text-purple-600 animate-spin mx-auto mb-4" />
          <p className="text-gray-600">Loading your pets...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-100 to-pink-100 py-8">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-800 mb-2">My Virtual Pets</h1>
            <p className="text-gray-600">Take care of your adorable companions!</p>
          </div>
          <button
            onClick={() => setIsCreateModalOpen(true)}
            className="flex items-center space-x-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white px-6 py-3 rounded-lg font-semibold hover:from-purple-700 hover:to-pink-700 transition-all transform hover:scale-105"
          >
            <Plus className="w-5 h-5" />
            <span>Add New Pet</span>
          </button>
        </div>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6">
            {error}
            <button
              onClick={fetchPets}
              className="ml-4 text-red-800 underline hover:no-underline"
            >
              Try again
            </button>
          </div>
        )}

        {pets.length === 0 ? (
          <div className="text-center py-16">
            <div className="text-6xl mb-4">🐾</div>
            <h2 className="text-2xl font-bold text-gray-800 mb-2">No pets yet!</h2>
            <p className="text-gray-600 mb-6">Create your first virtual pet to get started.</p>
            <button
              onClick={() => setIsCreateModalOpen(true)}
              className="bg-gradient-to-r from-purple-600 to-pink-600 text-white px-8 py-3 rounded-lg font-semibold hover:from-purple-700 hover:to-pink-700 transition-all transform hover:scale-105"
            >
              Create Your First Pet
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {pets.map((pet) => (
              <PetCard
                key={pet.id}
                pet={pet}
                onUpdate={handlePetUpdate}
                onDelete={handlePetDelete}
              />
            ))}
          </div>
        )}

        <CreatePetModal
          isOpen={isCreateModalOpen}
          onClose={() => setIsCreateModalOpen(false)}
          onPetCreated={handlePetCreated}
        />
      </div>
    </div>
  );
};

export default PetsList;