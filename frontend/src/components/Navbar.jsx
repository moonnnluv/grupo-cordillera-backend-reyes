import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-blue-700 text-white shadow-md">
      <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
        <Link to="/dashboard" className="text-xl font-bold tracking-wide hover:opacity-90">
          Grupo Cordillera
        </Link>

        <div className="flex items-center gap-4">
          {user && (
            <span className="text-sm hidden sm:block">
              {user.nombre ?? user.username}{' '}
              <span className="bg-blue-500 text-xs px-2 py-0.5 rounded-full ml-1">
                {user.role}
              </span>
            </span>
          )}
          <button
            onClick={handleLogout}
            className="bg-white text-blue-700 text-sm font-semibold px-3 py-1.5 rounded hover:bg-blue-50 transition"
          >
            Cerrar sesión
          </button>
        </div>
      </div>
    </nav>
  )
}
