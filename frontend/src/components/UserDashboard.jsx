export default function UserDashboard({ user }) {
  return (
    <div className="space-y-6">
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h2 className="text-lg font-semibold text-blue-700">
          Bienvenido, {user?.nombre ?? user?.username}
        </h2>
        <p className="text-sm text-blue-600 mt-1">Este es tu panel personal</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div className="bg-white rounded-lg shadow p-4">
          <h3 className="font-semibold text-gray-700 mb-2">Mis tareas</h3>
          <ul className="text-sm text-gray-600 space-y-1">
            <li className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-green-400 inline-block" />
              Revisar inventario
            </li>
            <li className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-yellow-400 inline-block" />
              Completar informe semanal
            </li>
            <li className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-gray-300 inline-block" />
              Reunión de equipo
            </li>
          </ul>
        </div>

        <div className="bg-white rounded-lg shadow p-4">
          <h3 className="font-semibold text-gray-700 mb-2">Mis datos</h3>
          <p className="text-sm text-gray-600">Usuario: {user?.username}</p>
          <p className="text-sm text-gray-600">Rol: {user?.role}</p>
          <p className="text-sm text-gray-600">Email: {user?.email ?? '—'}</p>
        </div>
      </div>
    </div>
  )
}
