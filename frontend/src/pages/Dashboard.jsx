import { useAuth } from '../context/AuthContext'
import Navbar from '../components/Navbar'
import AdminDashboard from '../components/AdminDashboard'
import UserDashboard from '../components/UserDashboard'

export default function Dashboard() {
  const { user } = useAuth()

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <main className="max-w-6xl mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Dashboard</h1>
        {user?.role === 'ADMIN' ? (
          <AdminDashboard user={user} />
        ) : (
          <UserDashboard user={user} />
        )}
      </main>
    </div>
  )
}
