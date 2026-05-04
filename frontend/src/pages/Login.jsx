import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const { data } = await api.post('/auth/login', form)
      login(data.user, data.token)
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message ?? 'Credenciales inválidas')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-sm p-8">
        <h1 className="text-2xl font-bold text-center text-blue-700 mb-6">Grupo Cordillera</h1>
        <h2 className="text-lg font-semibold text-gray-700 mb-4 text-center">Iniciar sesión</h2>

        {error && (
          <div className="bg-red-50 border border-red-300 text-red-600 text-sm rounded px-3 py-2 mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-600 mb-1">Usuario</label>
            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              required
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="tu_usuario"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-600 mb-1">Contraseña</label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="••••••••"
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-700 text-white font-semibold py-2 rounded-lg hover:bg-blue-800 transition disabled:opacity-60"
          >
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-500 mt-4">
          ¿No tienes cuenta?{' '}
          <Link to="/register" className="text-blue-600 hover:underline">
            Regístrate
          </Link>
        </p>
      </div>
    </div>
  )
}
